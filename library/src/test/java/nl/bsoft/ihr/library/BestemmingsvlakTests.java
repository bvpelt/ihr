package nl.bsoft.ihr.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Bestemmingsvlak;
import nl.bsoft.ihr.generated.model.BestemmingsvlakCollectie;
import nl.bsoft.ihr.generated.model.InfoGet200Response;
import nl.bsoft.ihr.library.mapper.BestemmingsvlakMapper;
import nl.bsoft.ihr.library.mapper.BestemmingsvlakMapperImpl;
import nl.bsoft.ihr.library.model.dto.BestemmingsvlakDto;
import nl.bsoft.ihr.library.service.APIService;

import org.apiguardian.api.API;
import org.checkerframework.checker.units.qual.A;
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
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BestemmingsvlakTests {
    private final BestemmingsvlakMapper bestemmingsvlakMapper = new BestemmingsvlakMapperImpl();
    private  APIService APIService;

    @Autowired
    private ResourceLoader resourceLoader = null;
    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    public BestemmingsvlakTests(APIService apiService) {
        this.APIService = apiService;
    }

    private static void checkResult(BestemmingsvlakDto bestemmingsvlakDto, Bestemmingsvlak bestemmingsvlak) {
        // check result
        Assert.equals(bestemmingsvlakDto.getIdentificatie(), bestemmingsvlak.getId());
        Assert.equals(bestemmingsvlakDto.getType(), bestemmingsvlak.getType().getValue());
        Assert.equals(bestemmingsvlakDto.getNaam(), bestemmingsvlak.getNaam());
        if (bestemmingsvlak.getBestemmingshoofdgroep().isPresent()) {
            Assert.equals(bestemmingsvlakDto.getBestemmingshoofdgroep(), bestemmingsvlak.getBestemmingshoofdgroep().get());
        } else {
            Assert.isTrue(bestemmingsvlakDto.getBestemmingshoofdgroep() == null);
        }
        if (bestemmingsvlak.getArtikelnummer().isPresent()) {
            Assert.equals(bestemmingsvlakDto.getArtikelnummer(), bestemmingsvlak.getArtikelnummer().get());
        } else {
            Assert.isTrue(bestemmingsvlakDto.getArtikelnummer() == null);
        }
        if (bestemmingsvlak.getLabelInfo().isPresent()) {
            Assert.equals(bestemmingsvlakDto.getLabelInfo(), bestemmingsvlak.getLabelInfo().get());
        } else {
            Assert.isTrue(bestemmingsvlak.getLabelInfo() == null);
        }
        if (bestemmingsvlak.getStyleId().isPresent()) {
            Assert.equals(bestemmingsvlakDto.getStyleid(), bestemmingsvlak.getStyleId().get());
        }
    }

    @Test
    @Order(1)
    public void checkVersion(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        InfoGet200Response info = getInfo();
        String app = info.getApp().toString();

        LinkedHashMap<String, String> keyValue = (LinkedHashMap<String, String>)info.getApp();

        String version = keyValue.get("version");
        log.info("version: {}", version);
        Assert.isTrue(version.equals("4.0.1"));
    }

    @Test
    @Order(2)
    public void apiTest_01(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        String planidentificatie = "NL.IMRO.0200.bp1253-vas1";
        String bestemmingsvlakidentificatie = "NL.IMRO.07420194a7e6471a94c1098e52a4769c";

        try {
            Bestemmingsvlak bestemmingsvlak = getBestemmingvlak(planidentificatie, bestemmingsvlakidentificatie);
            log.info("bestemmingsvlak: {}", bestemmingsvlak);

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

            checkResult(bestemmingsvlakDto, bestemmingsvlak);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
        }
    }

    @Test
    @Order(3)
    public void apiTest_02(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());

        String planidentificatie = "NL.IMRO.1705.178-VG01";
        String bestemmingsvlakidentificatie = "NL.IMRO.DB203342";

        try {
            Bestemmingsvlak bestemmingsvlak = getBestemmingvlak(planidentificatie, bestemmingsvlakidentificatie);
            log.info("bestemmingsvlak: {}", bestemmingsvlak);

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

            checkResult(bestemmingsvlakDto, bestemmingsvlak);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
        }
    }

    @Test
    @Order(4)
    public void apiTest_03(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());

        String planidentificatie = "NL.IMRO.1705.178-VG01";
        String bestemmingsvlakidentificatie = "NL.IMRO.EB203348";

        try {
            Bestemmingsvlak bestemmingsvlak = getBestemmingvlak(planidentificatie, bestemmingsvlakidentificatie);
            log.info("bestemmingsvlak: {}", bestemmingsvlak);

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

            checkResult(bestemmingsvlakDto, bestemmingsvlak);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
        }
    }

    @Test
    @Order(5)
    public void apiTest_04(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());

        String planidentificatie = "NL.IMRO.1705.178-VG01";
        String bestemmingsvlakidentificatie = "NL.IMRO.DB203340";

        try {
            Bestemmingsvlak bestemmingsvlak = getBestemmingvlak(planidentificatie, bestemmingsvlakidentificatie);
            log.info("bestemmingsvlak: {}", bestemmingsvlak);

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

            checkResult(bestemmingsvlakDto, bestemmingsvlak);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
        }
    }

    @Test
    @Order(6)
    public void apiTest_05(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());

        String planidentificatie = "NL.IMRO.1705.178-VG01";
        String bestemmingsvlakidentificatie = "NL.IMRO.DB203342";

        try {
            Bestemmingsvlak bestemmingsvlak = getBestemmingvlak(planidentificatie, bestemmingsvlakidentificatie);
            log.info("bestemmingsvlak: {}", bestemmingsvlak);

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

            checkResult(bestemmingsvlakDto, bestemmingsvlak);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
        }
    }

    @Test
    @Order(7)
    public void apiTest_06(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());

        String planidentificatie = "NL.IMRO.0014.BP506Ebbingekwarti-oh01";
        String bestemmingsvlakidentificatie = "NL.IMRO.0014.DP1229699208-00";

        try {
            Bestemmingsvlak bestemmingsvlak = getBestemmingvlak(planidentificatie, bestemmingsvlakidentificatie);
            log.info("bestemmingsvlak: {}", bestemmingsvlak);

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

            checkResult(bestemmingsvlakDto, bestemmingsvlak);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
        }
    }

    @Test
    public void mapBestemmingsvlakDto(TestInfo testInfo) {
        Bestemmingsvlak bestemmingsvlak;
        log.info("Start test: {} bestemmingsvlak.json", testInfo.getDisplayName());
        try {
            File dataFile = resourceLoader.getResource("classpath:bestemmingsvlak.json").getFile();

            bestemmingsvlak = objectMapper.readValue(dataFile, Bestemmingsvlak.class);
            log.info("tekst: \n{}", bestemmingsvlak.toString());

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

            checkResult(bestemmingsvlakDto, bestemmingsvlak);

        } catch (IOException e) {
            log.error("IO Error in mapBestemmingsvlakDto test: {}", e);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
        }
        log.info("End   tekst.json");
    }

    @Test
    public void mapBestemmingsvlakDto_01(TestInfo testInfo) {
        Bestemmingsvlak bestemmingsvlak;
        log.info("Start test: {} bestemmingsvlak-01.json", testInfo.getDisplayName());
        try {
            File dataFile = resourceLoader.getResource("classpath:bestemmingsvlak-01.json").getFile();

            bestemmingsvlak = objectMapper.readValue(dataFile, Bestemmingsvlak.class);
            log.info("tekst: \n{}", bestemmingsvlak.toString());

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

            checkResult(bestemmingsvlakDto, bestemmingsvlak);
        } catch (IOException e) {
            log.error("IO Error in mapBestemmingsvlakDto test: {}", e);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
        }
        log.info("End   tekst.json");
    }

    @Test
    public void mapBestemmingsvlakDto_02(TestInfo testInfo) {
        Bestemmingsvlak bestemmingsvlak;
        log.info("Start test: {} bestemmingsvlak-02.json", testInfo.getDisplayName());
        try {
            File dataFile = resourceLoader.getResource("classpath:bestemmingsvlak-02.json").getFile();

            bestemmingsvlak = objectMapper.readValue(dataFile, Bestemmingsvlak.class);
            log.info("tekst: \n{}", bestemmingsvlak.toString());

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

            checkResult(bestemmingsvlakDto, bestemmingsvlak);
        } catch (IOException e) {
            log.error("IO Error in mapBestemmingsvlakDto test: {}", e);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
        }
        log.info("End   tekst.json");
    }

    @Test
    public void mapBestemmingsvlakDto_03(TestInfo testInfo) {
        Bestemmingsvlak bestemmingsvlak;
        log.info("Start test: {} bestemmingsvlak-03.json", testInfo.getDisplayName());
        try {
            File dataFile = resourceLoader.getResource("classpath:bestemmingsvlak-03.json").getFile();

            bestemmingsvlak = objectMapper.readValue(dataFile, Bestemmingsvlak.class);
            log.info("bestemmingsvlak: \n{}", bestemmingsvlak.toString());

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

            checkResult(bestemmingsvlakDto, bestemmingsvlak);
        } catch (IOException e) {
            log.error("IO Error in mapBestemmingsvlakDto test: {}", e);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
        }
        log.info("End   tekst.json");
    }

    @Test
    public void mapBestemmingsvlakDto_04(TestInfo testInfo) {
        Bestemmingsvlak bestemmingsvlak;
        log.info("Start test: {} bestemmingsvlak-04.json", testInfo.getDisplayName());
        try {
            File dataFile = resourceLoader.getResource("classpath:bestemmingsvlak-04.json").getFile();

            bestemmingsvlak = objectMapper.readValue(dataFile, Bestemmingsvlak.class);
            log.info("tekst: \n{}", bestemmingsvlak.toString());

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

            checkResult(bestemmingsvlakDto, bestemmingsvlak);
        } catch (IOException e) {
            log.error("IO Error in mapBestemmingsvlakDto test: {}", e);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
        }
        log.info("End   tekst.json");
    }

    @Test
    public void mapBestemmingsvlakDto_05(TestInfo testInfo) {
        Bestemmingsvlak bestemmingsvlak;
        log.info("Start test: {} bestemmingsvlak-05.json", testInfo.getDisplayName());
        try {
            File dataFile = resourceLoader.getResource("classpath:bestemmingsvlak-05.json").getFile();

            bestemmingsvlak = objectMapper.readValue(dataFile, Bestemmingsvlak.class);
            log.info("tekst: \n{}", bestemmingsvlak.toString());

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

            checkResult(bestemmingsvlakDto, bestemmingsvlak);
        } catch (IOException e) {
            log.error("IO Error in mapBestemmingsvlakDto test: {}", e);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
        }
        log.info("End   bestemmingsvlak-05.json");
    }

    private Bestemmingsvlak getBestemmingvlak(String planidentificatie, String bestemmingsvlakidentificatie) {
        Bestemmingsvlak bestemmingsvlak = null;
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/bestemmingsvlakken/" + bestemmingsvlakidentificatie);

        log.info("using url: {}", uriComponentsBuilder.build().toUri());
        bestemmingsvlak = APIService.getDirectly(uriComponentsBuilder.build().toUri(), Bestemmingsvlak.class);
        return bestemmingsvlak;
    }

    private InfoGet200Response getInfo() {
        InfoGet200Response info = null;
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/info");

        log.info("using url: {}", uriComponentsBuilder.build().toUri());
        info = APIService.getDirectly(uriComponentsBuilder.build().toUri(), InfoGet200Response.class);
        return info;
    }

}
