package nl.bsoft.ihr.library.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.library.exception.InternalServerErrorException;
import nl.bsoft.ihr.library.exception.NotFoundException;
import nl.bsoft.ihr.library.model.dto.ExternalRequestErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalRequestService {
    private static final double BYTES_MULTIPLIER = 1024.0;

    private final ObjectMapper objectMapper;

    @Getter
    private final HttpClient httpClient;

    private final Duration requestDuration;
    private String tempLastRequestBody;

    private static double getMbsFromByteArray(byte... bytes) {
        return new String(bytes, StandardCharsets.UTF_8).length() / BYTES_MULTIPLIER / BYTES_MULTIPLIER;
    }

    private static String getResponseBody(HttpResponse<byte[]> httpResponse) {
        return new String(httpResponse.body(), Charset.defaultCharset());
    }

    public <T> CompletableFuture<T> get(URI uri, Class<T> clazz) {
        return get(request(uri), clazz);
    }

    public <T> CompletableFuture<T> getIgnoreFailure(URI uri, Class<T> clazz) {
        return get(request(uri), clazz)
                .exceptionallyAsync(
                        e -> {
                            handleIgnoreResponse(e, uri);
                            return null;
                        });
    }

    private <T> CompletableFuture<T> get(HttpRequest.Builder request, Class<T> clazz) {
        tempLastRequestBody = null;
        return performHttpRequest(request.GET().build(), clazz);
    }

    public <T> CompletableFuture<T> post(URI uri, String bodyJson, Class<T> clazz) {
        return post(request(uri), bodyJson, clazz);
    }

    private <T> CompletableFuture<T> post(
            HttpRequest.Builder request, String bodyJson, Class<T> clazz) {
        tempLastRequestBody = bodyJson;
        return performHttpRequest(
                request.POST(HttpRequest.BodyPublishers.ofString(bodyJson)).build(), clazz);
    }

    public <T> T getDirectly(URI uri, Class<T> clazz) {
        return getDirectlyRequest(request(uri), clazz);
    }

    public <T> T getDirectlyIgnoreFailure(URI uri, Class<T> clazz) {
        return getDirectlyRequestIgnoreFailure(request(uri), clazz);
    }

    public <T> T getDirectlyRequest(HttpRequest.Builder request, Class<T> clazz) {
        return getCompletableFuture(get(request, clazz));
    }

    public <T> T getDirectlyRequestIgnoreFailure(HttpRequest.Builder request, Class<T> clazz) {
        return getCompletableFuture(
                get(request, clazz)
                        .exceptionallyAsync(
                                e -> {
                                    handleIgnoreResponse(e, request.build().uri());
                                    return null;
                                }));
    }

    public <T> T postDirectly(URI uri, String bodyJson, Class<T> clazz) {
        return getCompletableFuture(post(request(uri), bodyJson, clazz));
    }

    public <T> T postDirectlyIgnoreFailure(URI uri, String bodyJson, Class<T> clazz) {
        return getCompletableFuture(
                post(request(uri), bodyJson, clazz)
                        .exceptionallyAsync(
                                e -> {
                                    handleIgnoreResponse(e, uri);
                                    return null;
                                }));
    }

    public <T> T getCompletableFuture(CompletableFuture<T> future) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error(e.getMessage());
            throw new InternalServerErrorException(e);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }

    public String objectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    protected HttpRequest.Builder request(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json, application/hal+json")
                .timeout(requestDuration);
    }

    private void handleIgnoreResponse(Throwable e, URI uri) {
        log.error("HttpClient url: {} error: {}", uri, e.getMessage());
    }

    private <T> CompletableFuture<T> performHttpRequest(HttpRequest httpRequest, Class<T> clazz) {
        log.debug(
                "Performing request on URL: {}, Headers: {}",
                httpRequest,
                httpRequest.headers().toString());
        return httpClient
                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(responseBody -> responseBody(httpRequest, responseBody, clazz))
                .exceptionallyAsync(
                        e -> {
                            log.error("HttpClient url: {} error: {}", httpRequest, e.getMessage());
                            throw new InternalServerErrorException(e.getMessage());
                        });
    }


    private <T> T responseBody(
            HttpRequest httpRequest, HttpResponse<byte[]> httpResponse, Class<T> clazz) {
        if (clazz == byte[].class) {
            return (T) httpResponse.body();
        }
        double bodyMegabyteSize = getMbsFromByteArray(httpResponse.body());
        if (bodyMegabyteSize > 3) {
            log.info("Finish mb's: {} fetching: {}", bodyMegabyteSize, httpRequest.uri().toString());
        }
        if (httpResponse.statusCode() != HttpStatus.OK.value()) {
            responseHandlerError(httpRequest, httpResponse);
        }
        return responseHandler(httpResponse, clazz);
    }

    private <T> T responseHandler(HttpResponse<byte[]> httpResponse, Class<T> clazz) {
        String responseBody = getResponseBody(httpResponse);
        try {
            return jsonMapper(responseBody, clazz);
        } catch (JsonProcessingException e) {
            log.error(
                    "bericht: {}, uri: {} body: {}",
                    e.getMessage(),
                    httpResponse.uri().toString(),
                    responseBody);
        }
        return null;
    }

    private void responseHandlerError(HttpRequest request, HttpResponse<byte[]> httpResponse) {
        String responseBody = getResponseBody(httpResponse);

        ExternalRequestErrorDTO externalRequestErrorDTO =
                responseHandler(httpResponse, ExternalRequestErrorDTO.class);
        String message =
                String.format(
                        "%d Unknown error %s on %s.",
                        httpResponse.statusCode(), responseBody, httpResponse.uri().toString());
        if (externalRequestErrorDTO != null) {
            message =
                    String.format(
                            "%d External request exception on: %s with status: %s with message: %s response body: %s",
                            httpResponse.statusCode(),
                            httpResponse.uri(),
                            httpResponse.statusCode(),
                            externalRequestErrorDTO.detail(),
                            responseBody);
        }
        logError(request, httpResponse);
        if (httpResponse.statusCode() == HttpStatus.NOT_FOUND.value()) {
            throw new NotFoundException(message);
        }
        throw new InternalServerErrorException(message);
    }

    private void logError(HttpRequest request, HttpResponse<byte[]> httpResponse) {
        log.error(
                "Error URL: {}, Headers: {} request body: {}, response body: {}",
                request,
                request.headers().toString(),
                tempLastRequestBody,
                getResponseBody(httpResponse));
    }

    private <T> T jsonMapper(String responseBody, Class<T> clazz) throws JsonProcessingException {
        return objectMapper
                .disable(
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // In case the generated models fails
                .readValue(responseBody, clazz);
    }
}