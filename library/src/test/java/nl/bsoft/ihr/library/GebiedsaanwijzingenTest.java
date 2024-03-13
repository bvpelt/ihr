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
    public void mapGebiedsaanwijzingDto() {
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
}
