package nl.bsoft.ihr.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.*;
import nl.bsoft.ihr.library.mapper.*;
import nl.bsoft.ihr.library.model.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import java.io.File;

@Slf4j
@SpringBootTest
public class GebiedsaanwijzingenTest {
    private final GebiedsaanduidingMapper gebiedsaanduidingMapper = new GebiedsaanduidingMapperImpl();

    @Autowired
    private ResourceLoader resourceLoader = null;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void mapGebiedsaanwijzing_00() {
        Gebiedsaanduiding gebiedsaanduiding;
        log.info("Start gebiedsaanwijzing-00.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:gebiedsaanwijzing-00.json").getFile();

            gebiedsaanduiding = objectMapper.readValue(dataFile, Gebiedsaanduiding.class);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            Assert.isTrue(gebiedsaanduidingDto.getIdentificatie().equals(gebiedsaanduiding.getId()), "Identification not equal");

        } catch (Exception e) {
            log.error("Error in mapGebiedsaanwijzingDto test: {}", e);
        }
        log.info("End   gebiedsaanwijzing-00.json");
    }

    @Test
    public void mapGebiedsaanwijzing_01() {
        Gebiedsaanduiding gebiedsaanduiding;
        log.info("Start gebiedsaanwijzing-01.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:gebiedsaanwijzing-01.json").getFile();

            gebiedsaanduiding = objectMapper.readValue(dataFile, Gebiedsaanduiding.class);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            Assert.isTrue(gebiedsaanduidingDto.getIdentificatie().equals(gebiedsaanduiding.getId()), "Identification not equal");

        } catch (Exception e) {
            log.error("Error in mapGebiedsaanwijzingDto test: {}", e);
        }
        log.info("End   gebiedsaanwijzing-01.json");
    }
    @Test
    public void mapGebiedsaanwijzing_02() {
        Gebiedsaanduiding gebiedsaanduiding;
        log.info("Start gebiedsaanwijzing-02.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:gebiedsaanwijzing-02.json").getFile();

            gebiedsaanduiding = objectMapper.readValue(dataFile, Gebiedsaanduiding.class);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            Assert.isTrue(gebiedsaanduidingDto.getIdentificatie().equals(gebiedsaanduiding.getId()), "Identification not equal");

        } catch (Exception e) {
            log.error("Error in mapGebiedsaanwijzingDto test: {}", e);
        }
        log.info("End   gebiedsaanwijzing-02.json");
    }
    @Test
    public void mapGebiedsaanwijzing_03() {
        Gebiedsaanduiding gebiedsaanduiding;
        log.info("Start gebiedsaanwijzing-03.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:gebiedsaanwijzing-03.json").getFile();

            gebiedsaanduiding = objectMapper.readValue(dataFile, Gebiedsaanduiding.class);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            Assert.isTrue(gebiedsaanduidingDto.getIdentificatie().equals(gebiedsaanduiding.getId()), "Identification not equal");

        } catch (Exception e) {
            log.error("Error in mapGebiedsaanwijzingDto test: {}", e);
        }
        log.info("End   gebiedsaanwijzing-03.json");
    }
    @Test
    public void mapGebiedsaanwijzing_04() {
        Gebiedsaanduiding gebiedsaanduiding;
        log.info("Start gebiedsaanwijzing-04.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:gebiedsaanwijzing-04.json").getFile();

            gebiedsaanduiding = objectMapper.readValue(dataFile, Gebiedsaanduiding.class);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            Assert.isTrue(gebiedsaanduidingDto.getIdentificatie().equals(gebiedsaanduiding.getId()), "Identification not equal");

        } catch (Exception e) {
            log.error("Error in mapGebiedsaanwijzingDto test: {}", e);
        }
        log.info("End   gebiedsaanwijzing-04.json");
    }
    @Test
    public void mapGebiedsaanwijzing_05() {
        Gebiedsaanduiding gebiedsaanduiding;
        log.info("Start gebiedsaanwijzing-05.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:gebiedsaanwijzing-05.json").getFile();

            gebiedsaanduiding = objectMapper.readValue(dataFile, Gebiedsaanduiding.class);
            log.info("plan: \n{}", gebiedsaanduiding.toString());

            GebiedsaanduidingDto gebiedsaanduidingDto = gebiedsaanduidingMapper.toGebiedsaanduiding(gebiedsaanduiding);
            log.info("gebiedsaanduidingDto: \n{}", gebiedsaanduidingDto.toString());
            Assert.isTrue(gebiedsaanduidingDto.getIdentificatie().equals(gebiedsaanduiding.getId()), "Identification not equal");

        } catch (Exception e) {
            log.error("Error in mapGebiedsaanwijzingDto test: {}", e);
        }
        log.info("End   gebiedsaanwijzing-05.json");
    }
}
