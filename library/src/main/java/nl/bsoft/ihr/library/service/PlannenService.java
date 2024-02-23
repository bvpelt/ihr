package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Plan;
import nl.bsoft.ihr.generated.model.PlanCollectie;
import nl.bsoft.ihr.generated.model.PlanCollectieEmbedded;
import nl.bsoft.ihr.library.mapper.PlanMapper;
import nl.bsoft.ihr.library.model.dto.ImroLoadDto;
import nl.bsoft.ihr.library.model.dto.PlanDto;
import nl.bsoft.ihr.library.repository.ImroLoadRepository;
import nl.bsoft.ihr.library.repository.PlanRepository;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PlannenService {
    private final int MAX_PAGE_SIZE;
    private final APIService APIService;
    private final PlanRepository planRepository;
    private final ImroLoadRepository imroLoadRepository;
    private final PlanMapper planMapper;

    @Autowired
    public PlannenService(APIService APIService, PlanRepository planRepository,  ImroLoadRepository imroLoadRepository, PlanMapper planMapper) {
        this.APIService = APIService;
        this.planRepository = planRepository;
        this.imroLoadRepository = imroLoadRepository;
        this.planMapper = planMapper;
        this.MAX_PAGE_SIZE = APIService.getMAX_PAGE_SIZE();
    }

    public PlanCollectie getPlannen(Integer page, Integer size) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen");
        uriComponentsBuilder.queryParam("page", page);
        uriComponentsBuilder.queryParam("pageSize", size);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), PlanCollectie.class);
    }

    /*
    correct 44*100 = 88*50
    correct 89*50 = 4450
    correct 446*10 = 4460
    error page 447
     */
    public UpdateCounter getAllPlannen() {
        int page = 46;
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
                if (planCollectieEmbedded != null) {
                    List<Plan> planList = planCollectieEmbedded.getPlannen();
                    planList.forEach(plan -> {
                        addPlan(plan, updateCounter);
                    });
                    if (planCollectie.getLinks().getNext() == null) {
                        morePages = false;
                    }
                } else {
                    morePages = false;
                }
            }
            page++;
        }
        return updateCounter;
    }

    public PlanDto addPlan(Plan plan, UpdateCounter updateCounter) {
        PlanDto savedPlan = null;

        PlanDto planDto = null;
        try {
            planDto = planMapper.toPlan(plan);

            Optional<PlanDto> optionalPlanDto = planRepository.findByIdentificatie(planDto.getIdentificatie());

            boolean changed = false;
            if (optionalPlanDto.isPresent()) {
                // copy current value
                PlanDto original = optionalPlanDto.get();

                log.trace("[IHR] add plan \n  Original: {}\n  New     : {}", original, planDto);
                if (original.equals(planDto)) {
                    log.trace("[IHR] equal");
                    planDto = original;
                } else {
                    changed = true;
                    log.trace("[IHR] not equal");
                    planDto = updatePlanDto(original, planDto);
                }
            }

            savedPlan = planRepository.save(planDto);

            if (optionalPlanDto.isPresent()) {
                if (changed) {
                    updateCounter.updated();
                } else {
                    updateCounter.add();
                }
            } else {
                updateCounter.add();
            }

            log.info("[IHR] plan {}", planDto);
        } catch (Exception e) {
            updateCounter.skipped();
            log.error("Error converting plan\n{}", e);
        }
        return savedPlan;
    }

    private PlanDto updatePlanDto(PlanDto original, PlanDto planDto) {
        PlanDto updatedPlan = original;
        original.setNaam(planDto.getNaam());
        original.setBesluitNummer(planDto.getBesluitNummer());
        original.setPlantype(planDto.getPlantype());
        original.setPlanstatus(planDto.getPlanstatus());
        original.setPlanstatusdate(planDto.getPlanstatusdate());
        original.setRegelstatus(planDto.getRegelstatus());
        original.setDossierid(planDto.getDossierid());
        original.setDossierstatus(planDto.getDossierstatus());

        return original;
    }

    public Plan getPlan(String identificatie) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + identificatie);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), Plan.class);
    }
    private void procesPlan(String identificatie, ImroLoadDto imroPlan, UpdateCounter updateCounter) {
        Plan plan = getPlan(identificatie);
        PlanDto savedPlan = addPlan(plan, updateCounter);

        imroPlan.setLoaded(true);
        imroLoadRepository.save(imroPlan);
    }
    public UpdateCounter loadPlannen() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByIdentificatieNotLoaded();

        imroLoadDtos.forEach(
                imroPlan -> {
                    procesPlan(imroPlan.getIdentificatie(), imroPlan, updateCounter);
                }
        );

        return updateCounter;
    }
}
