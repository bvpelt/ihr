package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.*;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.mapper.PlanMapper;
import nl.bsoft.ihr.library.model.dto.*;
import nl.bsoft.ihr.library.repository.*;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.metamodel.mapping.ordering.OrderByFragmentImpl;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class PlannenService {
    private final int MAX_PAGE_SIZE;
    private final APIService APIService;
    private final TekstenService tekstenService;
    private final BestemmingsvlakkenService bestemmingsvlakkenService;
    private final StructuurVisieGebiedService structuurVisieGebiedService;
    private final PlanRepository planRepository;
    private final ImroLoadRepository imroLoadRepository;
    private final LocatieRepository locatieRepository;
    private final LocatieNaamRepository locatieNaamRepository;
    private final OverheidRepository overheidRepository;
    private final PlanMapper planMapper;
    private final LocatieMapper locatieMapper;

    @Autowired
    public PlannenService(APIService APIService,
                          TekstenService tekstenService,
                          BestemmingsvlakkenService bestemmingsvlakkenService,
                          StructuurVisieGebiedService structuurVisieGebiedService,
                          PlanRepository planRepository,
                          ImroLoadRepository imroLoadRepository,
                          LocatieRepository locatieRepository,
                          LocatieNaamRepository locatieNaamRepository,
                          OverheidRepository overheidRepository,
                          PlanMapper planMapper,
                          LocatieMapper locatieMapper
    ) {
        this.APIService = APIService;
        this.tekstenService = tekstenService;
        this.bestemmingsvlakkenService = bestemmingsvlakkenService;
        this.structuurVisieGebiedService = structuurVisieGebiedService;
        this.planRepository = planRepository;
        this.imroLoadRepository = imroLoadRepository;
        this.locatieRepository = locatieRepository;
        this.locatieNaamRepository = locatieNaamRepository;
        this.overheidRepository = overheidRepository;
        this.planMapper = planMapper;
        this.locatieMapper = locatieMapper;
        this.MAX_PAGE_SIZE = APIService.getMAX_PAGE_SIZE();
    }

    public PlanCollectie getPlannen(Integer page, Integer size) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen");
        uriComponentsBuilder.queryParam("page", page);
        uriComponentsBuilder.queryParam("pageSize", size);
        String[] expand = {"geometrie"};
        uriComponentsBuilder.queryParam("expand", expand.toString());
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
                if (planCollectieEmbedded != null) {
                    List<Plan> planList = planCollectieEmbedded.getPlannen();
                    planList.forEach(plan -> {
                        addPlan(plan, null, updateCounter);
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

    public PlanDto addPlan(Plan plan, ImroLoadDto imroPlan, UpdateCounter updateCounter) {
        PlanDto savedPlan = null;

        PlanDto planDto = null;

        try {
            planDto = planMapper.toPlan(plan);
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

            Optional<PlanDto> optionalFoundPlanDto = planRepository.findByIdentificatie(planDto.getIdentificatie());

            boolean changed = false;
            if (optionalFoundPlanDto.isPresent()) {
                // copy current value
                PlanDto foundPlanDto = optionalFoundPlanDto.get();

                log.trace("[IHR] add plan \n  Original: {}\n  New     : {}", foundPlanDto, planDto);
                if (foundPlanDto.equals(planDto)) {
                    log.trace("[IHR] equal");
                    planDto = foundPlanDto;
                } else {
                    changed = true;
                    log.trace("[IHR] not equal");
                    planDto = updatePlanDto(foundPlanDto, planDto);
                }
            }

            log.debug("working on plan: {}", planDto);
   //         planRepository.save(planDto);


            PlanBeleidsmatigVerantwoordelijkeOverheid beleidsmatigeOverheid = plan.getBeleidsmatigVerantwoordelijkeOverheid();


            // if not found
            //   save
            // else
            //   do nothing
            //
            if (beleidsmatigeOverheid.getCode().isPresent()) {
                Optional<OverheidDto> OptionalBeleidsmatigOverheid = overheidRepository.findByCode(beleidsmatigeOverheid.getCode().get());
                OverheidDto currentBeleidsMatigeOverheid = null;
                if (OptionalBeleidsmatigOverheid.isPresent()) {
                    currentBeleidsMatigeOverheid = OptionalBeleidsmatigOverheid.get();
                    currentBeleidsMatigeOverheid.getBeleidsmatig().add(planDto);
                } else {
                    currentBeleidsMatigeOverheid = new OverheidDto();
                    currentBeleidsMatigeOverheid.setNaam(beleidsmatigeOverheid.getNaam().get());
                    currentBeleidsMatigeOverheid.setCode(beleidsmatigeOverheid.getCode().get());
                    currentBeleidsMatigeOverheid.setType(beleidsmatigeOverheid.getType().getValue());
                    currentBeleidsMatigeOverheid.getBeleidsmatig().add(planDto);
                }
                currentBeleidsMatigeOverheid = overheidRepository.save(currentBeleidsMatigeOverheid);
                planDto.getBeleidsmatigeoverheid().add(currentBeleidsMatigeOverheid);
                log.debug("beleidsmatige overheid: {}", currentBeleidsMatigeOverheid);
            }
            // save beleidsmatige overheden
            /*
            planDto.getBeleidsmatigeoverheid().forEach(beleidsmatigeoverheid -> {
                // if not found
                //   save
                // else
                //   do nothing
                //
                Optional<OverheidDto> optionalOverheidDto = overheidRepository.findByCode(beleidsmatigeoverheid.getCode());
                OverheidDto current = null;
                if (optionalOverheidDto.isPresent()) {
                    current = optionalOverheidDto.get();
                   // current.getBeleidsmatig().add(fixedPlanDto);
                    beleidsmatigeoverheid = current;
                    log.debug("existing overheid: {}", beleidsmatigeoverheid);
                } else {
                        current = new OverheidDto();
                        current.setNaam(beleidsmatigeoverheid.getNaam());
                        current.setCode(beleidsmatigeoverheid.getCode());
                        current.setType(beleidsmatigeoverheid.getType());
                        beleidsmatigeoverheid = overheidRepository.save(current);
                    log.debug("new overheid: {}", beleidsmatigeoverheid);
                }
            });
             */

            JsonNullable<PlanPublicerendBevoegdGezag> puOverheidDto = plan.getPublicerendBevoegdGezag();

            // if not found
            //   save
            // else
            //   do nothing
            //
            if (puOverheidDto.isPresent()) {
                if (puOverheidDto.get().getCode().isPresent()) {
                    Optional<OverheidDto> optionalPublicerendeOverheid = overheidRepository.findByCode(puOverheidDto.get().getCode().get());
                    OverheidDto currentPublicerendeOverheid = null;
                    if (optionalPublicerendeOverheid.isPresent()) {
                        currentPublicerendeOverheid = optionalPublicerendeOverheid.get();
                        currentPublicerendeOverheid.getBeleidsmatig().add(planDto);
                    } else {
                        currentPublicerendeOverheid = new OverheidDto();
                        currentPublicerendeOverheid.setNaam(puOverheidDto.get().getNaam().get());
                        currentPublicerendeOverheid.setCode(puOverheidDto.get().getCode().get());
                        currentPublicerendeOverheid.setType(puOverheidDto.get().getType().getValue());
                        currentPublicerendeOverheid.getBeleidsmatig().add(planDto);
                    }
                    currentPublicerendeOverheid = overheidRepository.save(currentPublicerendeOverheid);
                    planDto.getPublicerendeoverheid().add(currentPublicerendeOverheid);
                    log.debug("publicerende overheid: {}", currentPublicerendeOverheid);
                }
            }


            /*
            // save publicerende overheden
            planDto.getPublicerendeoverheid().forEach(publicerendeoverheid -> {
                // if not found
                //   save
                // else
                //   do nothing
                //
                Optional<OverheidDto> optionalOverheidDto = overheidRepository.findByCode(publicerendeoverheid.getCode());
                OverheidDto current = null;
                if (optionalOverheidDto.isPresent()){
                    current = optionalOverheidDto.get();
                //    current.getPublicerend().add(fixedPlanDto);
                    publicerendeoverheid = current;
                    log.debug("existing overheid: {}", publicerendeoverheid);
                } else {
                    current = new OverheidDto();
                    current.setNaam(publicerendeoverheid.getNaam());
                    current.setCode(publicerendeoverheid.getCode());
                    current.setType(publicerendeoverheid.getType());
                    publicerendeoverheid = overheidRepository.save(current);
                    log.debug("new overheid: {}", publicerendeoverheid);
                }
            });
             */


            List<String> locatieNaamDtoSet = plan.getLocatienamen();
            Iterator<String> locatieDtoIterable= locatieNaamDtoSet.iterator();
            while (locatieDtoIterable.hasNext()) {
                String puLocatieNaam = locatieDtoIterable.next();

                // if not found
                //   save
                // else
                //   do nothing
                //
                Optional<LocatieNaamDto> optionalOverheidDto = locatieNaamRepository.findByNaam(puLocatieNaam);
                LocatieNaamDto current = null;
                if (optionalOverheidDto.isPresent()) {
                    current = optionalOverheidDto.get();
                    current.getPlannen().add(planDto);
                } else {
                    current = new LocatieNaamDto();
                    current.setNaam(puLocatieNaam);
                    current.getPlannen().add(planDto);
                }
                current = locatieNaamRepository.save(current);
                planDto.getLocaties().add(current);
                log.debug("locatie: {}", current);
            }
            /*
            // save locatienamen
            planDto.getLocatienamen().forEach(locatienaam -> {
                // if not found
                //   save
                // else
                //   do nothing
                //
                Optional<LocatieNaamDto> optionalLocatieNaamDto = locatieNaamRepository.findByNaam(locatienaam.getNaam());
                LocatieNaamDto current = null;
                if (optionalLocatieNaamDto.isPresent()) {
                    current = optionalLocatieNaamDto.get();
                 //   current.getPlannen().add(fixedPlanDto);
                    locatienaam = current;
                    log.debug("existing locatie: {}", locatienaam);
                } else {
                    current = new LocatieNaamDto();
                    current.setNaam(locatienaam.getNaam());
                    locatienaam = locatieNaamRepository.save(current);
                    log.debug("new locatie: {}", locatienaam);
                }
            });
             */

            savedPlan = planRepository.save(planDto);

            if (optionalFoundPlanDto.isPresent()) {
                if (changed) {
                    updateCounter.updated();
                } else {
                    updateCounter.add();
                }
            } else {
                updateCounter.add();
            }

            UpdateCounter tekstCounter = new UpdateCounter();
            tekstenService.procesTekst(savedPlan.getIdentificatie(), 1, tekstCounter);
            log.info("processed tekst: {}", tekstCounter);
            if (imroPlan != null) {
                if (tekstCounter.getProcessed() > 0) {
                    imroPlan.setTekstenLoaded(true);
                }
            }

            UpdateCounter bestemmingsvlakCounter = new UpdateCounter();
            bestemmingsvlakkenService.procesBestemmingsvlak(savedPlan.getIdentificatie(), 1, bestemmingsvlakCounter);
            log.info("processed bestemmingsvlak: {}", bestemmingsvlakCounter);
            if (imroPlan != null) {
                if (bestemmingsvlakCounter.getProcessed() > 0) {
                    imroPlan.setBestemmingsvlakkenloaded(true);
                }
            }

            UpdateCounter structuurvisieCounter = new UpdateCounter();
            structuurVisieGebiedService.procesStructuurVisieGebied(savedPlan.getIdentificatie(), 1, structuurvisieCounter);
            log.info("processed structuurvisiegebied: {}", structuurvisieCounter);
            if (imroPlan != null) {
                if (structuurvisieCounter.getProcessed() > 0) {
                    imroPlan.setStructuurvisiegebiedloaded(true);
                }
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
        original.setPlantype(planDto.getPlantype());
        original.setBeleidsmatigeoverheid(planDto.getBeleidsmatigeoverheid());
        original.setPublicerendeoverheid(planDto.getPublicerendeoverheid());
        original.setNaam(planDto.getNaam());
        original.setLocaties(planDto.getLocaties());
        original.setPlanstatus(planDto.getPlanstatus());
        original.setPlanstatusdate(planDto.getPlanstatusdate());
        original.setBesluitNummer(planDto.getBesluitNummer());
        original.setRegelstatus(planDto.getRegelstatus());
        original.setDossierid(planDto.getDossierid());
        original.setDossierstatus(planDto.getDossierstatus());
        original.setIsParapluPlan(planDto.getIsParapluPlan());
        original.setBeroepEnBezwaar(planDto.getBeroepEnBezwaar());
        original.setMd5hash(planDto.getMd5hash());

        return original;
    }

    public Plan getPlan(String identificatie) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + identificatie);
        String[] expand = {"geometrie"};
        for (String param: expand) {
            uriComponentsBuilder.queryParam("expand", param);
        }
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), Plan.class);
    }

    private void procesPlan(ImroLoadDto imroPlan, UpdateCounter updateCounter) {
        Plan plan = null;

        try {
            plan = getPlan(imroPlan.getIdentificatie());
            PlanDto savedPlan = addPlan(plan, imroPlan, updateCounter);
            log.trace("Saved plan: {}", savedPlan.toString());

            imroPlan.setLoaded(true);
            imroLoadRepository.save(imroPlan);
        } catch (Exception e) {
            log.error("Plan mogelijk niet gevonden. {}", e);
        }
    }

    public UpdateCounter loadPlannen() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByIdentificatieNotLoaded();

        imroLoadDtos.forEach(
                imroPlan -> {
                    procesPlan(imroPlan, updateCounter);
                }
        );

        return updateCounter;
    }

    public UpdateCounter loadTekstenFromList() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByIdentificatieNotLoaded();

        imroLoadDtos.forEach(
                imroPlan -> {
                    tekstenService.procesTekst(imroPlan.getIdentificatie(), 1, updateCounter);
                }
        );

        return updateCounter;
    }
}
