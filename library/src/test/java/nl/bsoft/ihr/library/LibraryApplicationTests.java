package nl.bsoft.ihr.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Plan;
import nl.bsoft.ihr.library.mapper.PlanMapper;
import nl.bsoft.ihr.library.mapper.PlanMapperImpl;
import nl.bsoft.ihr.library.model.dto.PlanDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.File;

@Slf4j
@SpringBootTest
public class LibraryApplicationTests {
    private final PlanMapper planMapper = new PlanMapperImpl();
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

        } catch (Exception e) {
            log.error("Error in mapPlanDto test: {}", e);
        }
    }
}
