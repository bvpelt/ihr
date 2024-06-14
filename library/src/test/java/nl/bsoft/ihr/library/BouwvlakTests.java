package nl.bsoft.ihr.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Bouwvlak;
import nl.bsoft.ihr.generated.model.InfoGet200Response;
import nl.bsoft.ihr.library.mapper.BouwvlakMapper;
import nl.bsoft.ihr.library.mapper.BouwvlakMapperImpl;
import nl.bsoft.ihr.library.model.dto.BouwvlakDto;
import nl.bsoft.ihr.library.service.APIService;
import org.junit.jupiter.api.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

@Slf4j
@ComponentScan("nl.bsoft.ihr.library")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class BouwvlakTests {
    private final BouwvlakMapper bouwvlakMapper = new BouwvlakMapperImpl();
    private final APIService APIService;

    @Autowired
    private ResourceLoader resourceLoader = null;
    @Autowired
    private ObjectMapper objectMapper;

    private final String apiVersion = "4.1.0";

    @Autowired
    public BouwvlakTests(APIService apiService) {
        this.APIService = apiService;
    }

    private static void checkResult(BouwvlakDto bouwvlakDto, Bouwvlak bouwvlak) {
        // check result
        Assert.equals(bouwvlakDto.getIdentificatie(), bouwvlak.getId(), "Identificatie not equal");
        Assert.equals(bouwvlakDto.getNaam(), bouwvlak.getNaam(), "Naam not equal");
        if (bouwvlak.getStyleId().isPresent()) {
            if (bouwvlak.getStyleId().get() != null) {
                Assert.isTrue(bouwvlakDto.getStyleid().equals(bouwvlak.getStyleId().get()), "Styleid nog equal");
            }
        }
    }

    @Test
    @Order(1)
    public void checkVersion(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        InfoGet200Response info = getInfo();
        String app = info.getApp().toString();

        LinkedHashMap<String, String> keyValue = (LinkedHashMap<String, String>) info.getApp();

        String version = keyValue.get("version");
        log.info("version: {}", version);
        Assert.isTrue(version.equals(apiVersion));
    }


    private Bouwvlak getBouwvlak(String planidentificatie, String bouwvlakidentificatie) {
        Bouwvlak bouwvlak = null;
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/bouwvlakken/" + bouwvlakidentificatie);

        log.info("using url: {}", uriComponentsBuilder.build().toUri());
        bouwvlak = APIService.getDirectly(uriComponentsBuilder.build().toUri(), Bouwvlak.class);
        return bouwvlak;
    }

    @Test
    @Order(2)
    public void apiTest_01(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        String planidentificatie = "NL.IMRO.0177.BPW20190008-ON01";
        String bestemmingsvlakidentificatie = "NL.IMRO.56b1e57ba4c14a3594868999f3ed6389";

        try {
            Bouwvlak bouwvlak = getBouwvlak(planidentificatie, bestemmingsvlakidentificatie);
            log.info("bouwvlak: {}", bouwvlak);

            BouwvlakDto bouwvlakDto = bouwvlakMapper.toBestemmingsvlak(bouwvlak);
            log.info("bouwvlakDto: \n{}", bouwvlakDto.toString());

            checkResult(bouwvlakDto, bouwvlak);
        } catch (ParseException e) {
            log.error("Parse Error in mapBouwvlakDto test: {}", e);
        }
    }

    @Test
    @Order(3)
    public void apiTest_02(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        String planidentificatie = "NL.IMRO.0177.BPW20190008-ON01";
        String bestemmingsvlakidentificatie = "NL.IMRO.629ba6e0e54c434e96117d02b2b6791f";

        try {
            Bouwvlak bouwvlak = getBouwvlak(planidentificatie, bestemmingsvlakidentificatie);
            log.info("bouwvlak: {}", bouwvlak);

            BouwvlakDto bouwvlakDto = bouwvlakMapper.toBestemmingsvlak(bouwvlak);
            log.info("bouwvlakDto: \n{}", bouwvlakDto.toString());

            checkResult(bouwvlakDto, bouwvlak);
        } catch (ParseException e) {
            log.error("Parse Error in mapBouwvlakDto test: {}", e);
        }
    }

    @Test
    public void mapBestemmingsvlakDto(TestInfo testInfo) {
        Bouwvlak bouwvlak;
        log.info("Start test: {} bestemmingsvlak.json", testInfo.getDisplayName());
        try {
            File dataFile = resourceLoader.getResource("classpath:bouwvlak-01.json").getFile();

            bouwvlak = objectMapper.readValue(dataFile, Bouwvlak.class);
            log.info("tekst: \n{}", bouwvlak.toString());

            BouwvlakDto bouwvlakDto = bouwvlakMapper.toBestemmingsvlak(bouwvlak);
            log.info("bouwvlakDto: \n{}", bouwvlakDto.toString());

            checkResult(bouwvlakDto, bouwvlak);

        } catch (IOException e) {
            log.error("IO Error in mapBouwlakDto test: {}", e);
        } catch (ParseException e) {
            log.error("Parse Error in mapBouwlakDto test: {}", e);
        }
        log.info("End   tekst.json");
    }

    @Test
    public void mapBestemmingsvlakDto_01(TestInfo testInfo) {
        Bouwvlak bouwvlak;
        log.info("Start test: {} bestemmingsvlak.json", testInfo.getDisplayName());
        try {
            File dataFile = resourceLoader.getResource("classpath:bouwvlak-02.json").getFile();

            bouwvlak = objectMapper.readValue(dataFile, Bouwvlak.class);
            log.info("tekst: \n{}", bouwvlak.toString());

            BouwvlakDto bouwvlakDto = bouwvlakMapper.toBestemmingsvlak(bouwvlak);
            log.info("bouwvlakDto: \n{}", bouwvlakDto.toString());

            checkResult(bouwvlakDto, bouwvlak);

        } catch (IOException e) {
            log.error("IO Error in mapBouwlakDto test: {}", e);
        } catch (ParseException e) {
            log.error("Parse Error in mapBouwlakDto test: {}", e);
        }
        log.info("End   tekst.json");
    }

    private InfoGet200Response getInfo() {
        InfoGet200Response info = null;
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/info");

        log.info("using url: {}", uriComponentsBuilder.build().toUri());
        info = APIService.getDirectly(uriComponentsBuilder.build().toUri(), InfoGet200Response.class);
        return info;
    }

}
