package nl.bsoft.ihr.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Plan;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.mapper.LocatieMapperImpl;
import nl.bsoft.ihr.library.mapper.PlanMapper;
import nl.bsoft.ihr.library.mapper.PlanMapperImpl;
import nl.bsoft.ihr.library.model.dto.LocatieDto;
import nl.bsoft.ihr.library.model.dto.LocatieNaamDto;
import nl.bsoft.ihr.library.model.dto.PlanDto;
import nl.bsoft.ihr.library.service.APIService;
import org.junit.jupiter.api.*;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Set;

@Slf4j
@ComponentScan("nl.bsoft.ihr.library")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class PlanTests {
    private final PlanMapper planMapper = new PlanMapperImpl();
    private final LocatieMapper locatieMapper = new LocatieMapperImpl();
    private final nl.bsoft.ihr.library.service.APIService APIService;
    @Autowired
    private ResourceLoader resourceLoader = null;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public PlanTests(APIService apiService) {
        this.APIService = apiService;
    }

    private static void checkResult(PlanDto planDto, Plan plan) {
        Assert.isTrue(planDto.getIdentificatie().equals(plan.getId()), "Identification not equal");
        Assert.isTrue(planDto.getPlantype().equals(plan.getType().getValue()), "plantype not equal");
        Assert.isTrue(planDto.getNaam().equals(plan.getNaam()), "naam not equal");
        if (plan.getVerwijzingNaarVaststellingsbesluit().isPresent()) {
            Assert.isTrue(planDto.getVerwijzingnaarvaststelling().equals(plan.getVerwijzingNaarVaststellingsbesluit().get()), "vaststellingsbesluit not equal");
        }
        Assert.isTrue(planDto.getVerwijzingnaargml().equals(plan.getVerwijzingNaarGml()), "verwijzingnaargml not equal");
        if (plan.getBesluitnummer().isPresent()) {
            Assert.isTrue(planDto.getBesluitnummer().equals(plan.getBesluitnummer().get()), "bestluitnummer not equal");
        }
        if (plan.getRegelStatus().isPresent()) {
            Assert.isTrue(planDto.getRegelstatus().equals(plan.getRegelStatus().get()), "regelstatus not equal");
        }
        if (plan.getDossier().isPresent() && plan.getDossier().get() != null) {
            Assert.isTrue(planDto.getDossierid().equals(plan.getDossier().get().getId()), "dossierid not equal");
        }
        if (plan.getDossier().isPresent() && plan.getDossier().get() != null && plan.getDossier().get().getStatus().isPresent()) {
            Assert.isTrue(planDto.getDossierstatus().equals(plan.getDossier().get().getStatus().get()), "dossierstatus not equal");
        }
        Assert.isTrue(planDto.getIshistorisch().equals(plan.getIsHistorisch()), "is historisch nog equal");
        if (plan.getVerwijderdOp().isPresent()) {
            if (plan.getVerwijderdOp().get() != null) {
                Assert.isTrue(planDto.getVerwijderdop().equals(plan.getVerwijderdOp().get().toLocalDateTime()), "verwijderd op not equal");
            }
        }
        Assert.isTrue(planDto.getIstamplan().equals(plan.getIsTamPlan()), "is tamplan not equal");
        if (plan.getEindeRechtsgeldigheid().isPresent()) {
            if (plan.getEindeRechtsgeldigheid().get() != null) {
                Assert.isTrue(planDto.getEinderechtsgeldigheid().equals(plan.getEindeRechtsgeldigheid().get()), "einderechtsgeldigheid not equal");
            }
        }
        Assert.isTrue(planDto.getIsparapluplan().equals(plan.getIsParapluplan()), "isparapluplan not equal");
        if (plan.getBeroepEnBezwaar().isPresent()) {
            Assert.isTrue(planDto.getBeroepenbezwaar().equals(plan.getBeroepEnBezwaar().get()), "beroepenbezwaar not equal");
        }
    }

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
            checkResult(planDto, plan);

            LocatieDto locatieDto = locatieMapper.toLocatieDto(plan);
            log.info("locatieDto: \n{}", locatieDto.toString());

        } catch (IOException e) {
            log.error("Error in mapPlanDto test: {}", e);
        } catch (ParseException e) {
            log.error("Error in mapPlanDto test: {}", e);
        }
        log.info("End   plan.json");
    }

    @Test
    public void mapPlan01Dto(TestInfo testInfo) {

        log.info("Start test: {}", testInfo.getDisplayName());
        Plan plan;

        try {
            File dataFile = resourceLoader.getResource("classpath:plan-01.json").getFile();

            plan = objectMapper.readValue(dataFile, Plan.class);
            log.info("plan: \n{}", plan.toString());

            PlanDto planDto = planMapper.toPlan(plan);
            log.info("plandto: \n{}", planDto.toString());
            checkResult(planDto, plan);

        } catch (IOException e) {
            log.error("Error in mapPlanDto test: {}", e);
        } catch (ParseException e) {
            log.error("Error in mapPlanDto test: {}", e);
        }
        log.info("End   plan-01.json");
    }

    @Test
    public void mapPlan02Dto(TestInfo testInfo) {

        log.info("Start test: {}", testInfo.getDisplayName());
        Plan plan;

        try {
            File dataFile = resourceLoader.getResource("classpath:plan-02.json").getFile();

            plan = objectMapper.readValue(dataFile, Plan.class);
            log.info("plan: \n{}", plan.toString());

            PlanDto planDto = planMapper.toPlan(plan);
            log.info("plandto: \n{}", planDto.toString());
            checkResult(planDto, plan);

        } catch (IOException e) {
            log.error("Error in mapPlanDto test: {}", e);
        } catch (ParseException e) {
            log.error("Error in mapPlanDto test: {}", e);
        }
        log.info("End   plan-02.json");
    }


    @Test
    @Order(1)
    public void mytest() {
        String planidentificatie = "NL.IMRO.0200.bp1253-vas1";

        try {
            Plan plan = getPlan(planidentificatie);
            PlanDto planDto = planMapper.toPlan(plan);
            log.info("plandto: \n{}", planDto.toString());
            checkResult(planDto, plan);

        } catch (ParseException e) {
            log.error("Error in mapPlanDto test: {}", e);
        }
    }

    private Plan getPlan(String planidentificatie) {
        Plan plan = null;
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie);

        log.info("using url: {}", uriComponentsBuilder.build().toUri());
        plan = APIService.getDirectly(uriComponentsBuilder.build().toUri(), Plan.class);
        return plan;
    }
}
