package nl.bsoft.ihr.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Plan;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.mapper.LocatieMapperImpl;
import nl.bsoft.ihr.library.mapper.PlanMapper;
import nl.bsoft.ihr.library.mapper.PlanMapperImpl;
import nl.bsoft.ihr.library.model.dto.LocatieDto;
import nl.bsoft.ihr.library.model.dto.PlanDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import java.io.File;

@Slf4j
@SpringBootTest
public class PlanTests {
    private final PlanMapper planMapper = new PlanMapperImpl();
    private final LocatieMapper locatieMapper = new LocatieMapperImpl();

    @Autowired
    private ResourceLoader resourceLoader = null;
    @Autowired
    private ObjectMapper objectMapper;

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
    public void mapPlan02Dto() {
        Plan plan;
        log.info("Start plan-02.json");
        try {
            File dataFile = resourceLoader.getResource("classpath:plan-02.json").getFile();

            plan = objectMapper.readValue(dataFile, Plan.class);
            log.info("plan: \n{}", plan.toString());

            PlanDto planDto = planMapper.toPlan(plan);
            log.info("plandto: \n{}", planDto.toString());

        } catch (Exception e) {
            log.error("Error in mapPlanDto test: {}", e);
        }
        log.info("End   plan-02.json");
    }
}
