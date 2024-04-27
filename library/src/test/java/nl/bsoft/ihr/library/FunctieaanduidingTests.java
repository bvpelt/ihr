package nl.bsoft.ihr.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Bouwvlak;
import nl.bsoft.ihr.generated.model.Functieaanduiding;
import nl.bsoft.ihr.generated.model.InfoGet200Response;
import nl.bsoft.ihr.library.mapper.BouwvlakMapper;
import nl.bsoft.ihr.library.mapper.BouwvlakMapperImpl;
import nl.bsoft.ihr.library.mapper.FunctieaanduidingMapper;
import nl.bsoft.ihr.library.mapper.FunctieaanduidingMapperImpl;
import nl.bsoft.ihr.library.model.dto.BouwvlakDto;
import nl.bsoft.ihr.library.model.dto.FunctieaanduidingDto;
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
public class FunctieaanduidingTests {
    private final FunctieaanduidingMapper functieaanduidingMapper = new FunctieaanduidingMapperImpl();
    private final APIService APIService;

    @Autowired
    private ResourceLoader resourceLoader = null;
    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    public FunctieaanduidingTests(APIService apiService) {
        this.APIService = apiService;
    }

    private static void checkResult(FunctieaanduidingDto functieaanduidingDto, Functieaanduiding functieaanduiding) {
        // check result
        Assert.equals(functieaanduidingDto.getIdentificatie(), functieaanduiding.getId(), "Identificatie not equal");
        Assert.equals(functieaanduidingDto.getNaam(), functieaanduiding.getNaam(), "Naam not equal");
        if (functieaanduiding.getLabelInfo().isPresent()) {
            if (functieaanduiding.getLabelInfo().get() != null) {
                Assert.isTrue(functieaanduidingDto.getLabelinfo().equals(functieaanduiding.getLabelInfo().get()), "Labelinfo not equal");
            }
        }
        if (functieaanduiding.getVerwijzingNaarTekst().isPresent()) {
            if (functieaanduiding.getVerwijzingNaarTekst().get() != null) {
                Assert.isTrue(functieaanduidingDto.getVerwijzingnaartekst().equals(functieaanduiding.getVerwijzingNaarTekst().get()), "VerwijzingNaarTekst not equal");
            }
        }
        if (functieaanduiding.getStyleId().isPresent()) {
            if (functieaanduiding.getStyleId().get() != null) {
                Assert.isTrue(functieaanduidingDto.getStyleid().equals(functieaanduiding.getStyleId().get()), "Styleid not equal");
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
        Assert.isTrue(version.equals("4.0.1"));
    }


    private Functieaanduiding getBouwvlak(String planidentificatie, String functieaanduidingidentificatie) {
        Functieaanduiding functieaanduiding = null;
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/functieaanduidingen/" + functieaanduidingidentificatie);

        log.info("using url: {}", uriComponentsBuilder.build().toUri());
        functieaanduiding = APIService.getDirectly(uriComponentsBuilder.build().toUri(), Functieaanduiding.class);
        return functieaanduiding;
    }

    @Test
    @Order(2)
    public void apiTest_01(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        String planidentificatie = "NL.IMRO.0301.bp0104Glastuinbouw-on01";
        String functieaanduidingidentificatie = "NL.IMRO.50a331ce82374b0ba9e754bf0a433cb7";

        try {
            Functieaanduiding functieaanduiding = getBouwvlak(planidentificatie, functieaanduidingidentificatie);
            log.info("functieaanduiding: {}", functieaanduiding);

            FunctieaanduidingDto functieaanduidingDto = functieaanduidingMapper.toFunctieaanduiding(functieaanduiding);
            log.info("functieaanduiding: \n{}", functieaanduidingDto.toString());

            checkResult(functieaanduidingDto, functieaanduiding);
        } catch (ParseException e) {
            log.error("Parse Error in mapBouwvlakDto test: {}", e);
        }
    }

    @Test
    @Order(3)
    public void apiTest_02(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        String planidentificatie = "NL.IMRO.0153.R20190037-0002";
        String functieaanduidingidentificatie = "NL.IMRO.2341be8a6f924deea395f6eb62dcbf68";

        try {
            Functieaanduiding functieaanduiding = getBouwvlak(planidentificatie, functieaanduidingidentificatie);
            log.info("functieaanduiding: {}", functieaanduiding);

            FunctieaanduidingDto functieaanduidingDto = functieaanduidingMapper.toFunctieaanduiding(functieaanduiding);
            log.info("functieaanduiding: \n{}", functieaanduidingDto.toString());

            checkResult(functieaanduidingDto, functieaanduiding);
        } catch (ParseException e) {
            log.error("Parse Error in mapBouwvlakDto test: {}", e);
        }
    }

    @Test
    public void mapBestemmingsvlakDto(TestInfo testInfo) {
        Functieaanduiding functieaanduiding;
        log.info("Start test: {} bestemmingsvlak.json", testInfo.getDisplayName());
        try {
            File dataFile = resourceLoader.getResource("classpath:functieaanduiding-01.json").getFile();

            functieaanduiding = objectMapper.readValue(dataFile, Functieaanduiding.class);
            log.info("functieaanduiding: \n{}", functieaanduiding.toString());

            FunctieaanduidingDto functieaanduidingDto = functieaanduidingMapper.toFunctieaanduiding(functieaanduiding);
            log.info("functieaanduidingDto: \n{}", functieaanduidingDto.toString());

            checkResult(functieaanduidingDto, functieaanduiding);

        } catch (IOException e) {
            log.error("IO Error in mapBouwlakDto test: {}", e);
        } catch (ParseException e) {
            log.error("Parse Error in mapBouwlakDto test: {}", e);
        }
        log.info("End   tekst.json");
    }

    @Test
    public void mapBestemmingsvlakDto_01(TestInfo testInfo) {
        Functieaanduiding functieaanduiding;
        log.info("Start test: {} bestemmingsvlak.json", testInfo.getDisplayName());
        try {
            File dataFile = resourceLoader.getResource("classpath:functieaanduiding-02.json").getFile();

            functieaanduiding = objectMapper.readValue(dataFile, Functieaanduiding.class);
            log.info("functieaanduiding: \n{}", functieaanduiding.toString());

            FunctieaanduidingDto functieaanduidingDto = functieaanduidingMapper.toFunctieaanduiding(functieaanduiding);
            log.info("functieaanduidingDto: \n{}", functieaanduidingDto.toString());

            checkResult(functieaanduidingDto, functieaanduiding);

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
