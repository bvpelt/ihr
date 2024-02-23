package nl.bsoft.ihr.synchroniseren;

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
import org.springframework.boot.test.context.SpringBootTest;
@Slf4j
@SpringBootTest
public class SynchroniserenApplicationTests {
    @Test
    void contextLoads() {
    }
}
