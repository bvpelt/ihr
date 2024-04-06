package nl.bsoft.ihr.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Bestemmingsvlak;
import nl.bsoft.ihr.generated.model.Structuurvisiegebied;
import nl.bsoft.ihr.library.mapper.BestemmingsvlakMapper;
import nl.bsoft.ihr.library.mapper.BestemmingsvlakMapperImpl;
import nl.bsoft.ihr.library.mapper.StructuurVisieGebiedMapper;
import nl.bsoft.ihr.library.mapper.StructuurVisieGebiedMapperImpl;
import nl.bsoft.ihr.library.model.dto.BestemmingsvlakDto;
import nl.bsoft.ihr.library.model.dto.StructuurVisieGebiedDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.File;

@Slf4j
@SpringBootTest
public class StructuurvisieGebiedTests {
   private final StructuurVisieGebiedMapper structuurVisieGebiedMapper = new StructuurVisieGebiedMapperImpl();

    @Autowired
    private ResourceLoader resourceLoader = null;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void mapStructuurVisieGebiedDto () {
        Structuurvisiegebied structuurvisiegebied;
        log.info("Start structuurvisiegebied.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:structuurvisiegebied.json").getFile();

            structuurvisiegebied = objectMapper.readValue(dataFile, Structuurvisiegebied.class);
            log.info("structuurvisiegebied: \n{}", structuurvisiegebied.toString());

            StructuurVisieGebiedDto structuurVisieGebiedDto = structuurVisieGebiedMapper.toStructuurVisieGebied(structuurvisiegebied);
            log.info("structuurVisieGebiedDto: \n{}", structuurVisieGebiedDto.toString());

        } catch (Exception e) {
            log.error("Error in map structuurVisieGebiedDto test: {}", e);
        }
        log.info("End   structuurvisiegebied.json");
    }
    @Test
    public void mapStructuurVisieGebiedDto_01 () {
        Structuurvisiegebied structuurvisiegebied;
        log.info("Start structuurvisiegebied-01.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:structuurvisiegebied-01.json").getFile();

            structuurvisiegebied = objectMapper.readValue(dataFile, Structuurvisiegebied.class);
            log.info("structuurvisiegebied: \n{}", structuurvisiegebied.toString());

            StructuurVisieGebiedDto structuurVisieGebiedDto = structuurVisieGebiedMapper.toStructuurVisieGebied(structuurvisiegebied);
            log.info("structuurVisieGebiedDto: \n{}", structuurVisieGebiedDto.toString());

        } catch (Exception e) {
            log.error("Error in map structuurVisieGebiedDto test: {}", e);
        }
        log.info("End   structuurvisiegebied-01.json");
    }

    @Test
    public void mapStructuurVisieGebiedDto_02 () {
        Structuurvisiegebied structuurvisiegebied;
        log.info("Start structuurvisiegebied-02.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:structuurvisiegebied-02.json").getFile();

            structuurvisiegebied = objectMapper.readValue(dataFile, Structuurvisiegebied.class);
            log.info("structuurvisiegebied: \n{}", structuurvisiegebied.toString());

            StructuurVisieGebiedDto structuurVisieGebiedDto = structuurVisieGebiedMapper.toStructuurVisieGebied(structuurvisiegebied);
            log.info("structuurVisieGebiedDto: \n{}", structuurVisieGebiedDto.toString());

        } catch (Exception e) {
            log.error("Error in map structuurVisieGebiedDto test: {}", e);
        }
        log.info("End   structuurvisiegebied-02.json");
    }
}
