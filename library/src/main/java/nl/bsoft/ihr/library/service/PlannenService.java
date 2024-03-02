package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.*;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.mapper.PlanMapper;
import nl.bsoft.ihr.library.mapper.TekstMapper;
import nl.bsoft.ihr.library.model.dto.*;
import nl.bsoft.ihr.library.repository.*;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PlannenService {
    private final int MAX_PAGE_SIZE;
    private final APIService APIService;
    private final PlanRepository planRepository;
    private final ImroLoadRepository imroLoadRepository;
    private final LocatieRepository locatieRepository;
    private final OverheidRepository overheidRepository;
    private final TekstRepository tekstRepository;
    private final PlanMapper planMapper;
    private final LocatieMapper locatieMapper;
    private final TekstMapper tekstMapper;
    @Autowired
    public PlannenService(APIService APIService,
                          PlanRepository planRepository,
                          ImroLoadRepository imroLoadRepository,
                          OverheidRepository overheidRepository,
                          TekstRepository tekstRepository,
                          LocatieRepository locatieRepository,
                          PlanMapper planMapper,
                          LocatieMapper locatieMapper,
                          TekstMapper tekstMapper) {
        this.APIService = APIService;
        this.planRepository = planRepository;
        this.imroLoadRepository = imroLoadRepository;
        this.overheidRepository = overheidRepository;
        this.tekstRepository = tekstRepository;
        this.locatieRepository = locatieRepository;
        this.planMapper = planMapper;
        this.locatieMapper = locatieMapper;
        this.tekstMapper = tekstMapper;
        this.MAX_PAGE_SIZE = APIService.getMAX_PAGE_SIZE();
    }

    public PlanCollectie getPlannen(Integer page, Integer size) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen");
        uriComponentsBuilder.queryParam("page", page);
        uriComponentsBuilder.queryParam("pageSize", size);
        String [] expand = { "geometrie" };
        uriComponentsBuilder.queryParam("expand", expand);
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
        OverheidDto beleid = null;
        OverheidDto publicerend = null;
        try {
            planDto = planMapper.toPlan(plan);
            beleid = planMapper.toBeleidOverheid(plan);
            publicerend = planMapper.toPublicerendOverheid(plan);

            //
            // Check if locatie is known
            // if known
            //    skip
            // else
            //    store locatie
            //
            String md5hash = DigestUtils.md5Hex(plan.getGeometrie().toString().toUpperCase());
            planDto.setMd5hash(md5hash);

            Optional<LocatieDto> optionalLocatieDto = locatieRepository.findByMd5hash(md5hash);
            if (!optionalLocatieDto.isPresent()) {
                LocatieDto locatieDto = locatieMapper.toLocatieDto(plan);
                locatieDto.setMd5hash(md5hash);
                locatieDto.setRegistratie(LocalDateTime.now());
                locatieRepository.save(locatieDto);
                log.debug("Added locatie: {}", md5hash);
            }
            //
            // check if overheid is known for this plan and beleid code -- always mandatory
            // if so
            //   if publicerend has same code
            //     check if database entry has beleid = true and publicerende = true
            //   if publicerend has different code
            //     add occurance
            Optional<OverheidDto> foundOverheid = overheidRepository.findByIdentificatieAndCode(beleid.getPlan_identificatie(), beleid.getCode());
            if (foundOverheid.isPresent()) {
                OverheidDto found = foundOverheid.get();
                if (!found.getBeleidsmatig()) {
                    found.setBeleidsmatig(true);
                }
                if ((publicerend.getCode() != null) && (publicerend.getCode().equals(beleid.getCode()))) {
                    found.setPublicerend(true);
                } else {
                    found.setPublicerend(false);
                }
                overheidRepository.save(found);
            } else {
                beleid.setBeleidsmatig(true);
                if ((publicerend.getCode() != null) && (publicerend.getCode().equals(beleid.getCode()))) {
                    beleid.setPublicerend(true);
                } else {
                    beleid.setPublicerend(false);
                }
                overheidRepository.save(beleid);
            }

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

            UpdateCounter tekstCounter = new UpdateCounter();
            procesTekst(savedPlan.getIdentificatie(), tekstCounter);

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
        original.setMd5hash(planDto.getMd5hash());

        return original;
    }

    public Plan getPlan(String identificatie) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + identificatie);
        String [] expand = { "geometrie" };
        uriComponentsBuilder.queryParam("expand", expand);
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

    public UpdateCounter loadTeksten() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByIdentificatieNotLoaded();

        imroLoadDtos.forEach(
                imroPlan -> {
                    procesTekst(imroPlan.getIdentificatie(), updateCounter);
                }
        );

        return updateCounter;
    }
    private void procesTekst(String identificatie, UpdateCounter updateCounter) {
        TekstCollectie teksten = getTeksten(identificatie);
        if (teksten != null) {
            teksten.getEmbedded().getTeksten().forEach(tekst -> {
                addTekst(identificatie, tekst, updateCounter);
            });
        }
    }

    public TekstCollectie getTeksten(String identificatie) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + identificatie + "/teksten");
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), TekstCollectie.class);
    }

    private TekstDto addTekst(String planidentificatie, Tekst tekst, UpdateCounter updateCounter) {
        TekstDto savedTekst = null;

        try {
            TekstDto current = tekstMapper.toTekst(tekst);
            current.setPlanidentificatie(planidentificatie);

            Optional<TekstDto> found = tekstRepository.findByPlanidentificatieAndTekstidentificatie(current.getPlanidentificatie(), current.getTekstidentificatie());

            if (found.isPresent()) {
                // if equal do not save
                if (found.equals(current)) {
                    updateCounter.skipped();
                } else { // if changed update
                    TekstDto updated = found.get();
                    updated.setTitel(current.getTitel());
                    updated.setInhoud(current.getInhoud());
                    updated.setExternLabel(current.getExternLabel());
                    updated.setVolgNummer(current.getVolgNummer());
                    updated.setExternHRef(current.getExternHRef());
                    updateCounter.updated();
                    current = updated;
                }
            } else { // new occurrence
                updateCounter.add();
            }
            savedTekst = tekstRepository.save(current);
        } catch (Exception e) {
            log.error("Error while processing: {} in tekst processing: {}", tekst, e);
        }
        return savedTekst;
    }
}
