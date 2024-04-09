package nl.bsoft.ihr.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Bestemmingsvlak;
import nl.bsoft.ihr.library.mapper.BestemmingsvlakMapper;
import nl.bsoft.ihr.library.mapper.BestemmingsvlakMapperImpl;
import nl.bsoft.ihr.library.model.dto.BestemmingsvlakDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.File;

@Slf4j
@SpringBootTest
public class BestemmingsvlakTests {
    //private final PlanMapper planMapper = new PlanMapperImpl();
    //private final TekstMapper tekstMapper = new TekstMapperImpl();
    //private final LocatieMapper locatieMapper = new LocatieMapperImpl();
    private final BestemmingsvlakMapper bestemmingsvlakMapper = new BestemmingsvlakMapperImpl();
    //private final StructuurVisieGebiedMapper structuurVisieGebiedMapper = new StructuurVisieGebiedMapperImpl();

    @Autowired
    private ResourceLoader resourceLoader = null;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void mapBestemmingsvlakDto() {
        Bestemmingsvlak bestemmingsvlak;
        log.info("Start bestemmingsvlak.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:bestemmingsvlak.json").getFile();

            bestemmingsvlak = objectMapper.readValue(dataFile, Bestemmingsvlak.class);
            log.info("tekst: \n{}", bestemmingsvlak.toString());

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

        } catch (Exception e) {
            log.error("Error in mapBestemmingsvlakDto test: {}", e);
        }
        log.info("End   tekst.json");
    }

    @Test
    public void mapBestemmingsvlakDto_01() {
        Bestemmingsvlak bestemmingsvlak;
        log.info("Start bestemmingsvlak-01.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:bestemmingsvlak-01.json").getFile();

            bestemmingsvlak = objectMapper.readValue(dataFile, Bestemmingsvlak.class);
            log.info("tekst: \n{}", bestemmingsvlak.toString());

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

        } catch (Exception e) {
            log.error("Error in mapBestemmingsvlakDto test: {}", e);
        }
        log.info("End   tekst.json");
    }

    @Test
    public void mapBestemmingsvlakDto_02() {
        Bestemmingsvlak bestemmingsvlak;
        log.info("Start bestemmingsvlak-02.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:bestemmingsvlak-02.json").getFile();

            bestemmingsvlak = objectMapper.readValue(dataFile, Bestemmingsvlak.class);
            log.info("tekst: \n{}", bestemmingsvlak.toString());

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

        } catch (Exception e) {
            log.error("Error in mapBestemmingsvlakDto test: {}", e);
        }
        log.info("End   tekst.json");
    }

    @Test
    public void mapBestemmingsvlakDto_03() {
        Bestemmingsvlak bestemmingsvlak;
        log.info("Start bestemmingsvlak-03.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:bestemmingsvlak-03.json").getFile();

            bestemmingsvlak = objectMapper.readValue(dataFile, Bestemmingsvlak.class);
            log.info("bestemmingsvlak: \n{}", bestemmingsvlak.toString());

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

        } catch (Exception e) {
            log.error("Error in mapBestemmingsvlakDto test: {}", e);
        }
        log.info("End   tekst.json");
    }

    @Test
    public void mapBestemmingsvlakDto_04() {
        Bestemmingsvlak bestemmingsvlak;
        log.info("Start bestemmingsvlak-04.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:bestemmingsvlak-04.json").getFile();

            bestemmingsvlak = objectMapper.readValue(dataFile, Bestemmingsvlak.class);
            log.info("tekst: \n{}", bestemmingsvlak.toString());

            BestemmingsvlakDto bestemmingsvlakDto = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            log.info("bestemmingsvlakDto: \n{}", bestemmingsvlakDto.toString());

        } catch (Exception e) {
            log.error("Error in mapBestemmingsvlakDto test: {}", e);
        }
        log.info("End   tekst.json");
    }

}
