package nl.bsoft.ihr.library.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class HttpClientConfig {
    public static final long DEFAULT_DURATION_REQUEST_SECONDS = 480;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final HttpClient httpClient =
            HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NEVER)
                    .executor(executorService)
                    .connectTimeout(Duration.ofSeconds(DEFAULT_DURATION_REQUEST_SECONDS))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
    @Autowired
    private Environment environment;

    @Bean
    public HttpClient getHttpClient() {
        return httpClient;
    }

    @Bean
    public Duration getDurationInSeconds() {
        if (isRunningTest()) {
            log.info("Is running in test mode.");
            return Duration.ofMillis(1);
        }
        return Duration.ofSeconds(DEFAULT_DURATION_REQUEST_SECONDS);
    }

    private boolean isRunningTest() {
        List<String> profiles = Arrays.asList(environment.getActiveProfiles());
        if (System.getenv("JENKINS_HOME") != null || profiles.contains("integration-unit-test")) {
            return true;
        }
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }

}
