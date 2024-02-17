package nl.bsoft.ihr.library.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.*;
import nl.bsoft.ihr.library.model.dto.PlanDto;
import nl.bsoft.ihr.library.repository.PlanRepository;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service

public class PlannenService {
    private int MAX_PAGE_SIZE;
    private APIService APIService;

    private PlanRepository planRepository;

    @Autowired
    public PlannenService(APIService APIService, PlanRepository planRepository) {
        this.APIService = APIService;
        this.planRepository = planRepository;
        this.MAX_PAGE_SIZE = APIService.getMAX_PAGE_SIZE();
    }

    public PlanCollectie getPlannen(Integer page, Integer size) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen");
        uriComponentsBuilder.queryParam("pageSize", size);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), PlanCollectie.class);
    }

    public UpdateCounter getAllPlannen() {
        int page = 1;
        UpdateCounter updateCounter = new UpdateCounter();

        log.info("Start synchronizing ihr");

        boolean morePages = true;
        while (morePages) {
            log.info("[IHR] page: {}", page);
            PlanCollectie planCollectie = getPlannen(page, MAX_PAGE_SIZE);
            if (planCollectie == null) {
                morePages = false;
            } else {
                PlanCollectieEmbedded planCollectieEmbedded = planCollectie.getEmbedded();
                List<Plan> planList = planCollectieEmbedded.getPlannen();
                planList.forEach(plan -> {
                    String id = plan.getId();
                    String naam = plan.getNaam();
                    String planType = plan.getType().getValue();
                    PlanstatusInfo planstatusInfo = plan.getPlanstatusInfo();
                    String planStatus = planstatusInfo.getPlanstatus().toString();
                    LocalDate planStatusDate = planstatusInfo.getDatum();

                    PlanDto planDto = new PlanDto(id, naam, planType, planStatus, planStatusDate);
                    addPlan(planDto);

                    log.info("[IHR] plan id: {} -- naam: {} -- plantype: {} -- planstatus: {} -- planstatusdate: {}", id, naam, planType, planStatus, planStatusDate.toString());
                });
                if (planCollectie.getLinks().getNext() != null) {
                    page++;
                } else {
                    morePages = false;
                }
            }
        }

        return updateCounter;
    }

    public PlanDto addPlan(PlanDto plan) {
        PlanDto savedPlan = planRepository.save(plan);
        return savedPlan;
    }
}
