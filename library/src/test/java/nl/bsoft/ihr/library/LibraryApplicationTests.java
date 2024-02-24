package nl.bsoft.ihr.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Plan;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.mapper.LocatieMapperImpl;
import nl.bsoft.ihr.library.mapper.PlanMapper;
import nl.bsoft.ihr.library.mapper.PlanMapperImpl;
import nl.bsoft.ihr.library.model.dto.LocatieDto;
import nl.bsoft.ihr.library.model.dto.OverheidDto;
import nl.bsoft.ihr.library.model.dto.PlanDto;
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
    }

    @Test
    public void mapPlan01Dto() {
        Plan plan;
        try {
            File dataFile = resourceLoader.getResource("classpath:plan-01.json").getFile();

            plan = objectMapper.readValue(dataFile, Plan.class);
            log.info("plan: \n{}", plan.toString());

            PlanDto planDto = planMapper.toPlan(plan);
            log.info("plandto: \n{}", planDto.toString());

        } catch (Exception e) {
            log.error("Error in mapPlanDto test: {}", e);
        }
    }
}
