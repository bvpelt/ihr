package nl.bsoft.ihr.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Bestemmingsvlak;
import nl.bsoft.ihr.generated.model.Plan;
import nl.bsoft.ihr.generated.model.Structuurvisiegebied;
import nl.bsoft.ihr.generated.model.Tekst;
import nl.bsoft.ihr.library.mapper.*;
import nl.bsoft.ihr.library.model.dto.*;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import java.io.File;

@Slf4j
@SpringBootTest
public class TekstTests {
    private final TekstMapper tekstMapper = new TekstMapperImpl();
    @Autowired
    private ResourceLoader resourceLoader = null;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void mapTekstDto() {
        Tekst tekst;
        log.info("Start tekst.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:tekst.json").getFile();

            tekst = objectMapper.readValue(dataFile, Tekst.class);
            log.info("tekst: \n{}", tekst.toString());

            TekstDto tekstDto = tekstMapper.toTekst(tekst);
            log.info("tekstDto: \n{}", tekstDto.toString());

        } catch (Exception e) {
            log.error("Error in mapTekstDto test: {}", e);
        }
        log.info("End   tekst.json");
    }
}
