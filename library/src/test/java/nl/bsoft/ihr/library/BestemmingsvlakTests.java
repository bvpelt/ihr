package nl.bsoft.ihr.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Bestemmingsvlak;
import nl.bsoft.ihr.library.mapper.BestemmingsvlakMapper;
import nl.bsoft.ihr.library.mapper.BestemmingsvlakMapperImpl;
import nl.bsoft.ihr.library.model.dto.BestemmingsvlakDto;
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
public class BestemmingsvlakTests {
    private final BestemmingsvlakMapper bestemmingsvlakMapper = new BestemmingsvlakMapperImpl();

    @Autowired
    private ResourceLoader resourceLoader = null;
    @Autowired
    private ObjectMapper objectMapper;

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
            Assert.equals(bestemmingsvlakDto.getStileid(), bestemmingsvlak.getStyleId().get());
        }
    }

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

            checkResult(bestemmingsvlakDto, bestemmingsvlak);

        } catch (IOException e) {
            log.error("IO Error in mapBestemmingsvlakDto test: {}", e);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
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

            checkResult(bestemmingsvlakDto, bestemmingsvlak);
        } catch (IOException e) {
            log.error("IO Error in mapBestemmingsvlakDto test: {}", e);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
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

            checkResult(bestemmingsvlakDto, bestemmingsvlak);
        } catch (IOException e) {
            log.error("IO Error in mapBestemmingsvlakDto test: {}", e);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
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

            checkResult(bestemmingsvlakDto, bestemmingsvlak);
        } catch (IOException e) {
            log.error("IO Error in mapBestemmingsvlakDto test: {}", e);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
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

            checkResult(bestemmingsvlakDto, bestemmingsvlak);
        } catch (IOException e) {
            log.error("IO Error in mapBestemmingsvlakDto test: {}", e);
        } catch (ParseException e) {
            log.error("Parse Error in mapBestemmingsvlakDto test: {}", e);
        }
        log.info("End   tekst.json");
    }

    @Test
    public void mapBestemmingsvlakDto_05() {
        Bestemmingsvlak bestemmingsvlak;
        log.info("Start bestemmingsvlak-05.json");
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
}
