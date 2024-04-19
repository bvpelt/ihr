package nl.bsoft.ihr.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Gebiedsaanduiding;
import nl.bsoft.ihr.library.mapper.GebiedsaanduidingMapper;
import nl.bsoft.ihr.library.mapper.GebiedsaanduidingMapperImpl;
import nl.bsoft.ihr.library.model.dto.GebiedsaanduidingDto;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;

@Slf4j
@SpringBootTest
public class GebiedsaanduidingTest {
    private final GebiedsaanduidingMapper gebiedsaanduidingMapper = new GebiedsaanduidingMapperImpl();

    @Autowired
    private ResourceLoader resourceLoader = null;
    @Autowired
    private ObjectMapper objectMapper;

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
    }

    @Test
    public void mapGebiedsaanduiding_00() {
        Gebiedsaanduiding gebiedsaanduiding;
        log.info("Start gebiedsaanduiding-00.json");
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
    public void mapGebiedsaanduiding_01() {
        Gebiedsaanduiding gebiedsaanduiding;
        log.info("Start gebiedsaanduiding-01.json");
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
    public void mapGebiedsaanduiding_02() {
        Gebiedsaanduiding gebiedsaanduiding;
        log.info("Start gebiedsaanduiding-02.json");
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
    public void mapGebiedsaanduiding_03() {
        Gebiedsaanduiding gebiedsaanduiding;
        log.info("Start gebiedsaanduiding-03.json");
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
    public void mapGebiedsaanduiding_04() {
        Gebiedsaanduiding gebiedsaanduiding;
        log.info("Start gebiedsaanduiding-04.json");
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
    public void mapGebiedsaanduiding_05() {
        Gebiedsaanduiding gebiedsaanduiding;
        log.info("Start gebiedsaanduiding-05.json");
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
}
