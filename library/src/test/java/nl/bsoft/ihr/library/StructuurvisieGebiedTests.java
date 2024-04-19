package nl.bsoft.ihr.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Structuurvisiegebied;
import nl.bsoft.ihr.library.mapper.StructuurVisieGebiedMapper;
import nl.bsoft.ihr.library.mapper.StructuurVisieGebiedMapperImpl;
import nl.bsoft.ihr.library.model.dto.StructuurVisieGebiedDto;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;

@Slf4j
@SpringBootTest
public class StructuurvisieGebiedTests {
    private final StructuurVisieGebiedMapper structuurVisieGebiedMapper = new StructuurVisieGebiedMapperImpl();

    @Autowired
    private ResourceLoader resourceLoader = null;
    @Autowired
    private ObjectMapper objectMapper;

    private static void checkResult(StructuurVisieGebiedDto structuurVisieGebiedDto, Structuurvisiegebied structuurvisiegebied) {
        Assert.isTrue(structuurVisieGebiedDto.getIdentificatie().equals(structuurvisiegebied.getId()), "identification not equal");
        Assert.isTrue(structuurVisieGebiedDto.getNaam().equals(structuurvisiegebied.getNaam()), "naam not equal");
    }

    @Test
    public void mapStructuurVisieGebiedDto() {
        Structuurvisiegebied structuurvisiegebied;
        log.info("Start structuurvisiegebied.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:structuurvisiegebied.json").getFile();

            structuurvisiegebied = objectMapper.readValue(dataFile, Structuurvisiegebied.class);
            log.info("structuurvisiegebied: \n{}", structuurvisiegebied.toString());

            StructuurVisieGebiedDto structuurVisieGebiedDto = structuurVisieGebiedMapper.toStructuurVisieGebied(structuurvisiegebied);
            log.info("structuurVisieGebiedDto: \n{}", structuurVisieGebiedDto.toString());
            checkResult(structuurVisieGebiedDto, structuurvisiegebied);


        } catch (IOException e) {
            log.error("IOError in map structuurVisieGebiedDto test: {}", e);
        } catch (ParseException e) {
            log.error("ParseError in map structuurVisieGebiedDto test: {}", e);
        }
        log.info("End   structuurvisiegebied.json");
    }


    @Test
    public void mapStructuurVisieGebiedDto_01() {
        Structuurvisiegebied structuurvisiegebied;
        log.info("Start structuurvisiegebied-01.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:structuurvisiegebied-01.json").getFile();

            structuurvisiegebied = objectMapper.readValue(dataFile, Structuurvisiegebied.class);
            log.info("structuurvisiegebied: \n{}", structuurvisiegebied.toString());

            StructuurVisieGebiedDto structuurVisieGebiedDto = structuurVisieGebiedMapper.toStructuurVisieGebied(structuurvisiegebied);
            log.info("structuurVisieGebiedDto: \n{}", structuurVisieGebiedDto.toString());
            checkResult(structuurVisieGebiedDto, structuurvisiegebied);

        } catch (Exception e) {
            log.error("Error in map structuurVisieGebiedDto test: {}", e);
        }
        log.info("End   structuurvisiegebied-01.json");
    }

    @Test
    public void mapStructuurVisieGebiedDto_02() {
        Structuurvisiegebied structuurvisiegebied;
        log.info("Start structuurvisiegebied-02.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:structuurvisiegebied-02.json").getFile();

            structuurvisiegebied = objectMapper.readValue(dataFile, Structuurvisiegebied.class);
            log.info("structuurvisiegebied: \n{}", structuurvisiegebied.toString());

            StructuurVisieGebiedDto structuurVisieGebiedDto = structuurVisieGebiedMapper.toStructuurVisieGebied(structuurvisiegebied);
            log.info("structuurVisieGebiedDto: \n{}", structuurVisieGebiedDto.toString());
            checkResult(structuurVisieGebiedDto, structuurvisiegebied);

        } catch (Exception e) {
            log.error("Error in map structuurVisieGebiedDto test: {}", e);
        }
        log.info("End   structuurvisiegebied-02.json");
    }
}
