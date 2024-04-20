package nl.bsoft.ihr.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Gebiedsaanduiding;
import nl.bsoft.ihr.library.mapper.GebiedsaanduidingMapper;
import nl.bsoft.ihr.library.mapper.GebiedsaanduidingMapperImpl;
import nl.bsoft.ihr.library.model.dto.GebiedsaanduidingDto;
import nl.bsoft.ihr.library.service.APIService;
import org.junit.jupiter.api.*;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;

@Slf4j
@ComponentScan("nl.bsoft.ihr.library")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class GebiedsaanduidingTest {
    private final GebiedsaanduidingMapper gebiedsaanduidingMapper = new GebiedsaanduidingMapperImpl();
    private final APIService APIService;
    @Autowired
    private ResourceLoader resourceLoader = null;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public GebiedsaanduidingTest(APIService apiService) {
        this.APIService = apiService;
    }

    private static void checkResult(GebiedsaanduidingDto gebiedsaanduidingDto, Gebiedsaanduiding gebiedsaanduiding) {
        Assert.isTrue(gebiedsaanduidingDto.getIdentificatie().equals(gebiedsaanduiding.getId()), "Identification not equal");
        if (gebiedsaanduiding.getNaam() != null) {
            Assert.isTrue(gebiedsaanduidingDto.getNaam().equals(gebiedsaanduiding.getNaam()), "Naam not equal");
        }
        if (gebiedsaanduiding.getGebiedsaanduidinggroep().isPresent() && gebiedsaanduiding.getGebiedsaanduidinggroep().get() != null) {
            Assert.isTrue(gebiedsaanduidingDto.getGebiedsaanduidinggroep().equals(gebiedsaanduiding.getGebiedsaanduidinggroep().get()), "Gebiedsaanduidinggroep not equal");
        }
        if (gebiedsaanduiding.getLabelInfo().isPresent() && gebiedsaanduiding.getLabelInfo().get() != null) {
            Assert.isTrue(gebiedsaanduidingDto.getLabelinfo().equals(gebiedsaanduiding.getLabelInfo().get()), "Gebiedsaanduidinggroep not equal");
        }
        if (gebiedsaanduiding.getStyleId().isPresent() && gebiedsaanduiding.getStyleId().get() != null) {
            Assert.isTrue(gebiedsaanduidingDto.getStyleid().equals(gebiedsaanduiding.getStyleId().get()), "gebiedsaanduiding not equal");
        }
    }

    @Test
    @Order(1)
    public void gebiedsAanduidingTest_01(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        String planidentificatie = "NL.IMRO.0765.02BP00012010-0401";
        String gebiedsaanduidingidentificatie = "NL.IMRO.0765.GP2264677251-00";
        try {
            Gebiedsaanduiding gebiedsaanduiding = getGebiedsaanduiding(planidentificatie, gebiedsaanduidingidentificatie);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            checkResult(gebiedsaanduidingDto, gebiedsaanduiding);
        } catch (ParseException e) {
            log.error("ParseError in mapGebiedsaanduidingDto test: {}", e);
        }
    }

    @Test
    @Order(2)
    public void gebiedsAanduidingTest_02(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        String planidentificatie = "NL.IMRO.0765.02BP00012010-0401";
        String gebiedsaanduidingidentificatie = "NL.IMRO.0765.GP1191602860-00";
        try {
            Gebiedsaanduiding gebiedsaanduiding = getGebiedsaanduiding(planidentificatie, gebiedsaanduidingidentificatie);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            checkResult(gebiedsaanduidingDto, gebiedsaanduiding);
        } catch (ParseException e) {
            log.error("ParseError in mapGebiedsaanduidingDto test: {}", e);
        }
    }

    @Test
    @Order(3)
    public void gebiedsAanduidingTest_03(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        String planidentificatie = "NL.IMRO.0765.02BP00012010-0401";
        String gebiedsaanduidingidentificatie = "NL.IMRO.0765.GP1218555973-00";
        try {
            Gebiedsaanduiding gebiedsaanduiding = getGebiedsaanduiding(planidentificatie, gebiedsaanduidingidentificatie);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            checkResult(gebiedsaanduidingDto, gebiedsaanduiding);
        } catch (ParseException e) {
            log.error("ParseError in mapGebiedsaanduidingDto test: {}", e);
        }
    }

    @Test
    @Order(4)
    public void gebiedsAanduidingTest_04(TestInfo testInfo) {

        log.info("Start test: {}", testInfo.getDisplayName());
        String planidentificatie = "NL.IMRO.0765.02BP00012010-0401";
        String gebiedsaanduidingidentificatie = "NL.IMRO.0765.GP1264677249-00";
        try {
            Gebiedsaanduiding gebiedsaanduiding = getGebiedsaanduiding(planidentificatie, gebiedsaanduidingidentificatie);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            checkResult(gebiedsaanduidingDto, gebiedsaanduiding);
        } catch (ParseException e) {
            log.error("ParseError in mapGebiedsaanduidingDto test: {}", e);
        }
    }

    @Test
    @Order(5)
    public void gebiedsAanduidingTest_05(TestInfo testInfo) {

        log.info("Start test: {}", testInfo.getDisplayName());
        String planidentificatie = "NL.IMRO.0765.02BP00012010-0401";
        String gebiedsaanduidingidentificatie = "NL.IMRO.0765.GP2218556402-00";
        try {
            Gebiedsaanduiding gebiedsaanduiding = getGebiedsaanduiding(planidentificatie, gebiedsaanduidingidentificatie);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            checkResult(gebiedsaanduidingDto, gebiedsaanduiding);
        } catch (ParseException e) {
            log.error("ParseError in mapGebiedsaanduidingDto test: {}", e);
        }
    }

    @Test
    @Order(6)
    public void gebiedsAanduidingTest_06(TestInfo testInfo) {

        log.info("Start test: {}", testInfo.getDisplayName());
        String planidentificatie = "NL.IMRO.0765.02BP00012010-0401";
        String gebiedsaanduidingidentificatie = "NL.IMRO.0765.GP22264251766-00";
        try {
            Gebiedsaanduiding gebiedsaanduiding = getGebiedsaanduiding(planidentificatie, gebiedsaanduidingidentificatie);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            checkResult(gebiedsaanduidingDto, gebiedsaanduiding);
        } catch (ParseException e) {
            log.error("ParseError in mapGebiedsaanduidingDto test: {}", e);
        }
    }


    @Test
    public void mapGebiedsaanduiding_00(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        Gebiedsaanduiding gebiedsaanduiding;

        try {
            File dataFile = resourceLoader.getResource("classpath:gebiedsaanduiding-00.json").getFile();

            gebiedsaanduiding = objectMapper.readValue(dataFile, Gebiedsaanduiding.class);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            checkResult(gebiedsaanduidingDto, gebiedsaanduiding);
        } catch (IOException e) {
            log.error("IO Error in mapGebiedsaanduidingDto test: {}", e);
        } catch (ParseException e) {
            log.error("ParseError in mapGebiedsaanduidingDto test: {}", e);
        }
        log.info("End   gebiedsaanduiding-00.json");
    }


    @Test
    public void mapGebiedsaanduiding_01(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        Gebiedsaanduiding gebiedsaanduiding;

        try {
            File dataFile = resourceLoader.getResource("classpath:gebiedsaanduiding-01.json").getFile();

            gebiedsaanduiding = objectMapper.readValue(dataFile, Gebiedsaanduiding.class);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            checkResult(gebiedsaanduidingDto, gebiedsaanduiding);

        } catch (IOException e) {
            log.error("IO Error in mapGebiedsaanduidingDto test: {}", e);
        } catch (ParseException e) {
            log.error("ParseError in mapGebiedsaanduidingDto test: {}", e);
        }
        log.info("End   gebiedsaanduiding-01.json");
    }

    @Test
    public void mapGebiedsaanduiding_02(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        Gebiedsaanduiding gebiedsaanduiding;

        try {
            File dataFile = resourceLoader.getResource("classpath:gebiedsaanduiding-02.json").getFile();

            gebiedsaanduiding = objectMapper.readValue(dataFile, Gebiedsaanduiding.class);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            checkResult(gebiedsaanduidingDto, gebiedsaanduiding);

        } catch (IOException e) {
            log.error("IO Error in mapGebiedsaanduidingDto test: {}", e);
        } catch (ParseException e) {
            log.error("ParseError in mapGebiedsaanduidingDto test: {}", e);
        }
        log.info("End   gebiedsaanduiding-02.json");
    }

    @Test
    public void mapGebiedsaanduiding_03(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        Gebiedsaanduiding gebiedsaanduiding;

        try {
            File dataFile = resourceLoader.getResource("classpath:gebiedsaanduiding-03.json").getFile();

            gebiedsaanduiding = objectMapper.readValue(dataFile, Gebiedsaanduiding.class);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            checkResult(gebiedsaanduidingDto, gebiedsaanduiding);

        } catch (IOException e) {
            log.error("IO Error in mapGebiedsaanduidingDto test: {}", e);
        } catch (ParseException e) {
            log.error("ParseError in mapGebiedsaanduidingDto test: {}", e);
        }
        log.info("End   gebiedsaanduiding-03.json");
    }

    @Test
    public void mapGebiedsaanduiding_04(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        Gebiedsaanduiding gebiedsaanduiding;

        try {
            File dataFile = resourceLoader.getResource("classpath:gebiedsaanduiding-04.json").getFile();

            gebiedsaanduiding = objectMapper.readValue(dataFile, Gebiedsaanduiding.class);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            checkResult(gebiedsaanduidingDto, gebiedsaanduiding);

        } catch (IOException e) {
            log.error("IO Error in mapGebiedsaanduidingDto test: {}", e);
        } catch (ParseException e) {
            log.error("ParseError in mapGebiedsaanduidingDto test: {}", e);
        }
        log.info("End   gebiedsaanduiding-04.json");
    }

    @Test
    public void mapGebiedsaanduiding_05(TestInfo testInfo) {
        log.info("Start test: {}", testInfo.getDisplayName());
        Gebiedsaanduiding gebiedsaanduiding;

        try {
            File dataFile = resourceLoader.getResource("classpath:gebiedsaanduiding-05.json").getFile();

            gebiedsaanduiding = objectMapper.readValue(dataFile, Gebiedsaanduiding.class);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            checkResult(gebiedsaanduidingDto, gebiedsaanduiding);

        } catch (IOException e) {
            log.error("IO Error in mapGebiedsaanduidingDto test: {}", e);
        } catch (ParseException e) {
            log.error("ParseError in mapGebiedsaanduidingDto test: {}", e);
        }
        log.info("End   gebiedsaanduiding-05.json");
    }

    private Gebiedsaanduiding getGebiedsaanduiding(String planidentificatie, String gebiedsaanduidingidentificatie) {
        Gebiedsaanduiding gebiedsaanduiding = null;
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/gebiedsaanduidingen/" + gebiedsaanduidingidentificatie);

        log.info("using url: {}", uriComponentsBuilder.build().toUri());
        gebiedsaanduiding = APIService.getDirectly(uriComponentsBuilder.build().toUri(), Gebiedsaanduiding.class);
        return gebiedsaanduiding;
    }
}
