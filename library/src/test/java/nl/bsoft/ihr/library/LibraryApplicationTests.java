package nl.bsoft.ihr.library;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Plan;
import nl.bsoft.ihr.generated.model.Tekst;
import nl.bsoft.ihr.library.mapper.*;
import nl.bsoft.ihr.library.model.dto.LocatieDto;
import nl.bsoft.ihr.library.model.dto.OverheidDto;
import nl.bsoft.ihr.library.model.dto.PlanDto;
import nl.bsoft.ihr.library.model.dto.TekstDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import java.io.File;

@Slf4j
@SpringBootTest
public class LibraryApplicationTests {
    private final PlanMapper planMapper = new PlanMapperImpl();
    private final TekstMapper tekstMapper = new TekstMapperImpl();
    private final LocatieMapper locatieMapper = new LocatieMapperImpl();

    @Autowired
    private ResourceLoader resourceLoader = null;
    @Autowired
    private ObjectMapper objectMapper;

    /*
    @Autowired

    public LibraryApplicationTests(PlanMapperImpl planMapper) {
        this.planMapper = planMapper;
    }
*/
    @Test
    public void mapPlanDto() {
        Plan plan;
        log.info("Start plan.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:plan.json").getFile();

            plan = objectMapper.readValue(dataFile, Plan.class);
            log.info("plan: \n{}", plan.toString());

            PlanDto planDto = planMapper.toPlan(plan);
            log.info("plandto: \n{}", planDto.toString());
            Assert.isTrue(planDto.getIdentificatie().equals(plan.getId()), "Identification not equal");
            Assert.isTrue(planDto.getNaam().equals(plan.getNaam()), "Naam not equal");

            OverheidDto beleidOverheid = planMapper.toBeleidOverheid(plan);
            log.info("beleidOverheid: \n{}", beleidOverheid.toString());

            OverheidDto publicerendOverheid = planMapper.toPublicerendOverheid(plan);
            log.info("publicerendOverheid: \n{}", publicerendOverheid.toString());

            LocatieDto locatieDto = locatieMapper.toLocatieDto(plan);
            log.info("locatieDto: \n{}", locatieDto.toString());
        } catch (Exception e) {
            log.error("Error in mapPlanDto test: {}", e);
        }
        log.info("End   plan.json");
    }

    @Test
    public void mapPlan01Dto() {
        Plan plan;
        log.info("Start plan-01.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:plan-01.json").getFile();

            plan = objectMapper.readValue(dataFile, Plan.class);
            log.info("plan: \n{}", plan.toString());

            PlanDto planDto = planMapper.toPlan(plan);
            log.info("plandto: \n{}", planDto.toString());

        } catch (Exception e) {
            log.error("Error in mapPlanDto test: {}", e);
        }
        log.info("End   plan-01.json");
    }

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
