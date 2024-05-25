package nl.bsoft.ihr.library.service;


import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.*;
import nl.bsoft.ihr.library.exception.NotFoundException;
import nl.bsoft.ihr.library.mapper.*;
import nl.bsoft.ihr.library.model.dto.*;
import nl.bsoft.ihr.library.repository.*;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.apache.commons.codec.digest.DigestUtils;
import org.locationtech.jts.io.ParseException;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class PlannenService {
    public static final String VERVANGT_MET = "VervangtMet";
    public static final String TENGEVOLGE_VAN_MET = "TengevolgeVanMet";
    public static final String MUTEERT_MET = "MuteertMet";
    public static final String GEBRUIKT_INFORMATIE_UIT_MET = "GebruiktInformatieUitMet";
    public static final String GEDEELTELIJKE_HERZIENING_MET = "GedeeltelijkeHerzieningMet";
    public static final String UIT_TE_WERKEN_IN_MET = "UitTeWerkenInMet";
    public static final String UITGEWERKT_IN_MET = "UitgewerktInMet";
    public static final String VERVANGT_VANUIT = "VervangtVanuit";
    public static final String TEN_GEVOLGE_VAN_VANUIT = "TenGevolgeVanVanuit";
    public static final String MUTEERT_VANUIT = "MuteertVanuit";
    public static final String GEBRUIKT_INFORMATIE_UIT_VANUIT = "GebruiktInformatieUitVanuit";
    public static final String GEDEELTELIJKE_HERZIENING_VANUIT = "GedeeltelijkeHerzieningVanuit";
    public static final String UIT_TE_WERKEN_IN_VANUIT = "UitTeWerkenInVanuit";
    public static final String UITGEWERKINT_IN_VANUIT = "UitgewerkintInVanuit";
    private final int MAX_PAGE_SIZE;
    private final APIService APIService;
    private final TekstenService tekstenService;
    private final BestemmingsvlakkenService bestemmingsvlakkenService;
    private final BouwvlakkenService bouwvlakkenService;
    private final FunctieaanduidingService functieaanduidingService;
    private final BouwaanduidingenService bouwaanduidingService;
    private final LettertekenaanduidingService lettertekenaanduidingService;
    private final MaatvoeringenService maatvoeringService;
    private final FigurenService figuurService;
    private final StructuurVisieGebiedenService structuurVisieGebiedService;
    private final PlanRepository planRepository;
    private final ImroLoadRepository imroLoadRepository;
    private final LocatieRepository locatieRepository;
    private final LocatieNaamRepository locatieNaamRepository;
    private final PlanStatusRepository planStatusRepository;
    private final OverheidRepository overheidRepository;
    private final VerwijzingNormRepository verwijzingNormRepository;
    private final NormadressantRepository normadressantRepository;
    private final OndergrondRepository ondergrondRepository;
    private final ExternalPlanRepository externalPlanRepository;
    private final IllustratieRepository illustratieRepository;
    private final AuditLogRepository auditLogRepository;
    private final PlanMapper planMapper;
    private final LocatieMapper locatieMapper;
    private final ExternPlanMapper externPlanMapper;
    private final IllustratieMapper illustratieMapper;
    private final OndergrondMapper ondergrondMapper;

    @Autowired
    public PlannenService(APIService APIService,
                          TekstenService tekstenService,
                          BestemmingsvlakkenService bestemmingsvlakkenService,
                          BouwvlakkenService bouwvlakkenService,
                          FunctieaanduidingService functieaanduidingService,
                          BouwaanduidingenService bouwaanduidingService,
                          LettertekenaanduidingService lettertekenaanduidingService,
                          MaatvoeringenService maatvoeringService,
                          FigurenService figuurService,
                          StructuurVisieGebiedenService structuurVisieGebiedService,
                          PlanRepository planRepository,
                          ImroLoadRepository imroLoadRepository,
                          LocatieRepository locatieRepository,
                          LocatieNaamRepository locatieNaamRepository,
                          OverheidRepository overheidRepository,
                          PlanMapper planMapper,
                          LocatieMapper locatieMapper,
                          ExternPlanMapper externPlanMapper,
                          IllustratieMapper illustratieMapper,
                          OndergrondMapper ondergrondMapper,
                          PlanStatusRepository planStatusRepository,
                          VerwijzingNormRepository verwijzingNormRepository,
                          NormadressantRepository normadressantRepository,
                          OndergrondRepository ondergrondRepository,
                          ExternalPlanRepository externalPlanRepository,
                          IllustratieRepository illustratieRepository,
                          AuditLogRepository auditLogRepository
    ) {
        this.APIService = APIService;
        this.tekstenService = tekstenService;
        this.bestemmingsvlakkenService = bestemmingsvlakkenService;
        this.bouwvlakkenService = bouwvlakkenService;
        this.functieaanduidingService = functieaanduidingService;
        this.bouwaanduidingService = bouwaanduidingService;
        this.lettertekenaanduidingService = lettertekenaanduidingService;
        this.maatvoeringService = maatvoeringService;
        this.figuurService = figuurService;
        this.structuurVisieGebiedService = structuurVisieGebiedService;
        this.planRepository = planRepository;
        this.imroLoadRepository = imroLoadRepository;
        this.locatieRepository = locatieRepository;
        this.locatieNaamRepository = locatieNaamRepository;
        this.overheidRepository = overheidRepository;
        this.planMapper = planMapper;
        this.locatieMapper = locatieMapper;
        this.externPlanMapper = externPlanMapper;
        this.illustratieMapper = illustratieMapper;
        this.ondergrondMapper = ondergrondMapper;
        this.planStatusRepository = planStatusRepository;
        this.verwijzingNormRepository = verwijzingNormRepository;
        this.normadressantRepository = normadressantRepository;
        this.ondergrondRepository = ondergrondRepository;
        this.externalPlanRepository = externalPlanRepository;
        this.illustratieRepository = illustratieRepository;
        this.auditLogRepository = auditLogRepository;
        this.MAX_PAGE_SIZE = APIService.getMAX_PAGE_SIZE();
    }

    private static void updatePlan(PlanDto plan, String field, ExternPlanDto externPlan) {
        switch (field) {
            case VERVANGT_MET: {
                if (externPlan.getVervangtmetplan() == null) {
                    externPlan.setVervangtmetplan(plan);
                } else {
                    log.error("Vervangtmetplan plan: {} heeft al een waarde voor externplan {}", plan.getIdentificatie(), externPlan.getIdentificatie());
                }
            }
            break;
            case TENGEVOLGE_VAN_MET: {
                if (externPlan.getTengevolgevanmetplan() == null) {
                    externPlan.setTengevolgevanmetplan(plan);
                } else {
                    log.error("Tengevolgevanmetplan plan: {} heeft al een waarde voor externplan {}", plan.getIdentificatie(), externPlan.getIdentificatie());
                }
            }
            break;
            case MUTEERT_MET: {
                if (externPlan.getMuteertmetplan() == null) {
                    externPlan.setMuteertmetplan(plan);
                } else {
                    log.error("Muteertmetplan plan: {} heeft al een waarde voor externplan {}", plan.getIdentificatie(), externPlan.getIdentificatie());
                }
            }
            break;
            case GEBRUIKT_INFORMATIE_UIT_MET: {
                if (externPlan.getGebruiktinfouitmetplan() == null) {
                    externPlan.setGebruiktinfouitmetplan(plan);
                } else {
                    log.error("Gebruiktinfouitmetplan plan: {} heeft al een waarde voor externplan {}", plan.getIdentificatie(), externPlan.getIdentificatie());
                }
            }
            break;
            case GEDEELTELIJKE_HERZIENING_MET: {
                if (externPlan.getGedeeltelijkeherzieningmetplan() == null) {
                    externPlan.setGedeeltelijkeherzieningmetplan(plan);
                } else {
                    log.error("Gedeeltelijkeherzieningmetplan plan: {} heeft al een waarde voor externplan {}", plan.getIdentificatie(), externPlan.getIdentificatie());
                }
            }
            break;
            case UIT_TE_WERKEN_IN_MET: {
                if (externPlan.getUittewerkinginmetplan() == null) {
                    externPlan.setUittewerkinginmetplan(plan);
                } else {
                    log.error("Uittewerkinginmetplan plan: {} heeft al een waarde voor externplan {}", plan.getIdentificatie(), externPlan.getIdentificatie());
                }
            }
            break;
            case UITGEWERKT_IN_MET: {
                if (externPlan.getUitgewerktinmetplan() == null) {
                    externPlan.setUitgewerktinmetplan(plan);
                } else {
                    log.error("Uitgewerktinmetplan plan: {} heeft al een waarde voor externplan {}", plan.getIdentificatie(), externPlan.getIdentificatie());
                }
            }
            break;
            case VERVANGT_VANUIT: {
                if (externPlan.getVervangtvanuitplan() == null) {
                    externPlan.setVervangtvanuitplan(plan);
                } else {
                    log.error("Vervangtvanuitplan plan: {} heeft al een waarde voor externplan {}", plan.getIdentificatie(), externPlan.getIdentificatie());
                }
            }
            break;
            case TEN_GEVOLGE_VAN_VANUIT: {
                if (externPlan.getTegevolgevanvanuitplan() == null) {
                    externPlan.setTegevolgevanvanuitplan(plan);
                } else {
                    log.error("Tegevolgevanvanuitplan plan: {} heeft al een waarde voor externplan {}", plan.getIdentificatie(), externPlan.getIdentificatie());
                }
            }
            break;
            case MUTEERT_VANUIT: {
                if (externPlan.getMuteertvanuitplan() == null) {
                    externPlan.setMuteertvanuitplan(plan);
                } else {
                    log.error("Muteertvanuitplan plan: {} heeft al een waarde voor externplan {}", plan.getIdentificatie(), externPlan.getIdentificatie());
                }
            }
            break;
            case GEBRUIKT_INFORMATIE_UIT_VANUIT: {
                if (externPlan.getGebruiktinforuitvanuitplan() == null) {
                    externPlan.setGebruiktinforuitvanuitplan(plan);
                } else {
                    log.error("Gebruiktinforuitvanuitplan plan: {} heeft al een waarde voor externplan {}", plan.getIdentificatie(), externPlan.getIdentificatie());
                }
            }
            break;
            case GEDEELTELIJKE_HERZIENING_VANUIT: {
                if (externPlan.getGedeeltelijkeherzieningvanuitplan() == null) {
                    externPlan.setGedeeltelijkeherzieningvanuitplan(plan);
                } else {
                    log.error("Gedeeltelijkeherzieningvanuitplan plan: {} heeft al een waarde voor externplan {}", plan.getIdentificatie(), externPlan.getIdentificatie());
                }
            }
            break;
            case UIT_TE_WERKEN_IN_VANUIT: {
                if (externPlan.getUittewerkinginvanuitplan() == null) {
                    externPlan.setUittewerkinginvanuitplan(plan);
                } else {
                    log.error("Uittewerkinginvanuitplan plan: {} heeft al een waarde voor externplan {}", plan.getIdentificatie(), externPlan.getIdentificatie());
                }
            }
            break;
            case UITGEWERKINT_IN_VANUIT: {
                if (externPlan.getUitgewerktinvanuitplan() == null) {
                    externPlan.setUitgewerktinvanuitplan(plan);
                } else {
                    log.error("Uitgewerktinvanuitplan plan: {} heeft al een waarde voor externplan {}", plan.getIdentificatie(), externPlan.getIdentificatie());
                }
            }
            break;
            default:
                log.error("Field {} not found for plan: {}", field, plan.getIdentificatie());
        }
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
                        Optional<ImroLoadDto> foundImroPlan = imroLoadRepository.findByIdentificatie(plan.getId());
                        ImroLoadDto imroPlan;
                        if (foundImroPlan.isPresent()) {
                            imroPlan = foundImroPlan.get();
                        } else {
                            imroPlan = new ImroLoadDto();
                            imroPlan.setIdentificatie(plan.getId());
                        }
                        addPlan(plan, imroPlan, updateCounter);
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

    private PlanCollectie getPlannen(Integer page, Integer size) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen");
        uriComponentsBuilder.queryParam("page", page);
        uriComponentsBuilder.queryParam("pageSize", size);
        String[] expand = {"geometrie", "bbox"};
        for (String param : expand) {
            uriComponentsBuilder.queryParam("expand", param);
        }
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), PlanCollectie.class);
    }

    @Transactional
    protected PlanDto addPlan(Plan plan, ImroLoadDto imroPlan, UpdateCounter updateCounter) {
        PlanDto savedPlan = null;

        PlanDto planDto;

        try {
            String actie = "";
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
            String bboxMd5hash = DigestUtils.md5Hex(plan.getBbox().toString().toUpperCase());
            planDto.setBboxMd5hash(bboxMd5hash);

            extractLocation(plan, md5hash);
            extractLocationBbox(plan, bboxMd5hash);

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

            // manytoone relations
            extractPlanStatus(planDto);

            extractBeleidsmatigeOverheid(planDto);

            extractPublicerendeOverheid(planDto);

            planRepository.save(planDto); // reference for manytomany relations

            // onetomany

            extractRelatiesMetExternePlannen(plan, planDto);

            extractRelatiesVanuitExternePlannen(plan, planDto);

            extractIllustraties(plan, planDto);

            // manytomany relations
            extractLocatieNamen(plan, planDto);

            extractNormadressant(plan, planDto);

            extractVerwijzingNorm(plan, planDto);

            extractOndergrond(plan, planDto);

            savedPlan = planRepository.save(planDto);

            if (optionalFoundPlanDto.isPresent()) {
                if (changed) {
                    actie = "updated";
                    updateCounter.updated();
                } else {
                    actie = "add";
                    updateCounter.add();
                }
            } else {
                actie = "add";
                updateCounter.add();
            }

            UpdateCounter tekstCounter = new UpdateCounter();
            tekstenService.procesTeksten(savedPlan.getIdentificatie(), 1, tekstCounter, imroPlan);
            log.info("processed teksten: {}", tekstCounter);

            UpdateCounter bestemmingsvlakCounter = new UpdateCounter();
            bestemmingsvlakkenService.procesBestemmingsvlakken(savedPlan.getIdentificatie(), 1, bestemmingsvlakCounter, imroPlan);
            log.info("processed bestemmingsvlakken: {}", bestemmingsvlakCounter);

            UpdateCounter bouwvlakCounter = new UpdateCounter();
            bouwvlakkenService.procesBouwvlakken(savedPlan.getIdentificatie(), 1, bouwvlakCounter, imroPlan);
            log.info("processed bouwvlakken: {}", bouwvlakCounter);

            UpdateCounter functieaanduidingCounter = new UpdateCounter();
            functieaanduidingService.procesFunctieaanduidingen(savedPlan.getIdentificatie(), 1, functieaanduidingCounter, imroPlan);
            log.info("processed functieaanduidingen: {}", functieaanduidingCounter);

            UpdateCounter bouwaanduidingCounter = new UpdateCounter();
            bouwaanduidingService.procesBouwaanduidingen(savedPlan.getIdentificatie(), 1, bouwaanduidingCounter, imroPlan);
            log.info("processed bouwaanduidingen: {}", bouwaanduidingCounter);

            UpdateCounter lettertekenaanduidingCounter = new UpdateCounter();
            lettertekenaanduidingService.procesLettertekenaanduidingen(savedPlan.getIdentificatie(), 1, lettertekenaanduidingCounter, imroPlan);
            log.info("processed lettertekenaanduidingen: {}", lettertekenaanduidingCounter);

            UpdateCounter maatvoeringCounter = new UpdateCounter();
            maatvoeringService.procesMaatvoeringen(savedPlan.getIdentificatie(), 1, maatvoeringCounter, imroPlan);
            log.info("processed maatvoeringen: {}", maatvoeringCounter);

            UpdateCounter figuurCounter = new UpdateCounter();
            figuurService.procesFiguren(savedPlan.getIdentificatie(), 1, figuurCounter, imroPlan);
            log.info("processed figuren: {}", figuurCounter);

            UpdateCounter structuurvisieCounter = new UpdateCounter();
            structuurVisieGebiedService.procesStructuurVisieGebieden(savedPlan.getIdentificatie(), 1, structuurvisieCounter, imroPlan);
            log.info("processed structuurvisiegebied: {}", structuurvisieCounter);

            AuditLogDto auditLogDto = new AuditLogDto(savedPlan.getIdentificatie(), "plan", actie);
            auditLogRepository.save(auditLogDto);

            imroPlan.setPlanloaded(true);
            log.info("[IHR] plan {}", planDto);
        } catch (Exception e) {
            updateCounter.skipped();
            log.error("Error converting plan\n {}", e);
        }
        return savedPlan;
    }

    private void extractLocation(Plan plan, String md5hash) throws ParseException {
        Optional<LocatieDto> optionalLocatieDto = locatieRepository.findByMd5hash(md5hash);
        if (optionalLocatieDto.isEmpty()) {
            LocatieDto locatieDto = locatieMapper.toLocatieDto(plan);
            locatieDto.setMd5hash(md5hash);
            locatieDto.setRegistratie(LocalDateTime.now());
            locatieRepository.save(locatieDto);
            log.debug("Added locatie: {}", md5hash);
        }
    }

    private void extractLocationBbox(Plan plan, String md5hash) throws ParseException {
        Optional<LocatieDto> optionalLocatieDto = locatieRepository.findByMd5hash(md5hash);
        if (optionalLocatieDto.isEmpty()) {
            LocatieDto locatieDto = locatieMapper.toLocatieDtoBbox(plan);
            locatieDto.setMd5hash(md5hash);
            locatieDto.setRegistratie(LocalDateTime.now());
            locatieRepository.save(locatieDto);
            log.debug("Added locatie: {}", md5hash);
        }
    }

    private PlanDto updatePlanDto(PlanDto original, PlanDto planDto) {
        planDto.setPlantype(original.getPlantype());
        planDto.setBeleidsmatigeoverheid(original.getBeleidsmatigeoverheid());
        planDto.setPublicerendeoverheid(original.getPublicerendeoverheid());
        planDto.setNaam(original.getNaam());
        planDto.setLocaties(original.getLocaties());
        planDto.setPlanstatus(original.getPlanstatus());
        planDto.setVerwijzingnaarvaststelling(original.getVerwijzingnaarvaststelling());
        planDto.setVerwijzingnaargml(original.getVerwijzingnaargml());
        planDto.setBesluitnummer(original.getBesluitnummer());
        planDto.setVerwijzingnormen(original.getVerwijzingnormen());
        planDto.setNormadressanten(original.getNormadressanten());
        planDto.setOndergronden(original.getOndergronden());
        planDto.setRegelstatus(original.getRegelstatus());
        planDto.setDossierid(original.getDossierid());
        planDto.setDossierstatus(original.getDossierstatus());
        planDto.setIshistorisch(original.getIshistorisch());
        planDto.setVerwijderdop(original.getVerwijderdop());
        planDto.setIstamplan(original.getIstamplan());
        planDto.setEinderechtsgeldigheid(original.getEinderechtsgeldigheid());
        planDto.setIsparapluplan(original.getIsparapluplan());
        planDto.setBeroepenbezwaar(original.getBeroepenbezwaar());
        planDto.setMd5hash(original.getMd5hash());
        planDto.setBboxMd5hash(original.getBboxMd5hash());

        return planDto;
    }

    private void extractPlanStatus(PlanDto planDto) {
        PlanStatusDto planStatusDto = planDto.getPlanstatus();
        Optional<PlanStatusDto> optionalPlanStatusDto = planStatusRepository.findByStatusAndDatum(planStatusDto.getStatus(), planStatusDto.getDatum());
        if (optionalPlanStatusDto.isPresent()) {
            planStatusDto = optionalPlanStatusDto.get();
            planDto.setPlanstatus(planStatusDto);
        }
        planStatusDto.getPlannen().add(planDto);
        planStatusRepository.save(planStatusDto);
        log.info("Planstatus: {}", planStatusDto);
    }

    private void extractBeleidsmatigeOverheid(PlanDto planDto) {
        OverheidDto beleidsmatigeOverheid = planDto.getBeleidsmatigeoverheid();
        if (beleidsmatigeOverheid.getCode() != null) {
            Optional<OverheidDto> optionalOverheid = overheidRepository.findByCode(beleidsmatigeOverheid.getCode());
            if (optionalOverheid.isPresent()) {
                beleidsmatigeOverheid = optionalOverheid.get();
                planDto.setBeleidsmatigeoverheid(beleidsmatigeOverheid);
            }
            beleidsmatigeOverheid.getPublicerend().add(planDto);
            overheidRepository.save(beleidsmatigeOverheid);
        }
        log.debug("beleidsmatige overheid: {}", beleidsmatigeOverheid);
    }

    private void extractPublicerendeOverheid(PlanDto planDto) {
        OverheidDto publicerendeOverheid = planDto.getPublicerendeoverheid();
        if (publicerendeOverheid != null) {
            if (publicerendeOverheid.getCode() != null) {
                Optional<OverheidDto> optionalOverheid = overheidRepository.findByCode(publicerendeOverheid.getCode());
                if (optionalOverheid.isPresent()) {
                    publicerendeOverheid = optionalOverheid.get();
                    planDto.setPublicerendeoverheid(publicerendeOverheid);
                }
                publicerendeOverheid.getPublicerend().add(planDto);
                overheidRepository.save(publicerendeOverheid);
            }
        }
        log.debug("publicerende overheid: {}", publicerendeOverheid);
    }

    private void extractRelatiesMetExternePlannen(Plan plan, PlanDto planDto) {
        List<RelatieMetExternPlanReferentie> planList = plan.getRelatiesMetExternePlannen().getVervangt();
        if (planList != null && !planList.isEmpty()) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, VERVANGT_MET);
            planDto.setVervangtMetPlannen(vervangSet);
        }

        planList = plan.getRelatiesMetExternePlannen().getTenGevolgeVan();
        if (planList != null && !planList.isEmpty()) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, TENGEVOLGE_VAN_MET);
            planDto.setTengevolgeVanMetPlannen(vervangSet);
        }

        planList = plan.getRelatiesMetExternePlannen().getMuteert();
        if (planList != null && !planList.isEmpty()) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, MUTEERT_MET);
            planDto.setMuteertMetPlannen(vervangSet);
        }

        planList = plan.getRelatiesMetExternePlannen().getGebruiktInformatieUit();
        if (planList != null && !planList.isEmpty()) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, GEBRUIKT_INFORMATIE_UIT_MET);
            planDto.setGebruiktInfoUitMetPlannen(vervangSet);
        }

        planList = plan.getRelatiesMetExternePlannen().getGedeeltelijkeHerzieningVan();
        if (planList != null && !planList.isEmpty()) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, GEDEELTELIJKE_HERZIENING_MET);
            planDto.setGedeeltelijkeHerzieningMetPlannen(vervangSet);
        }

        planList = plan.getRelatiesMetExternePlannen().getUitTeWerkenIn();
        if (planList != null && !planList.isEmpty()) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, UIT_TE_WERKEN_IN_MET);
            planDto.setUitTeWerkenInMetPlannen(vervangSet);
        }

        planList = plan.getRelatiesMetExternePlannen().getUitgewerktIn();
        if (planList != null && !planList.isEmpty()) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, UITGEWERKT_IN_MET);
            planDto.setUitgewerktInMetPlannen(vervangSet);
        }
    }

    private void extractRelatiesVanuitExternePlannen(Plan plan, PlanDto planDto) {
        List<RelatieMetExternPlanReferentie> planList = plan.getRelatiesVanuitExternePlannen().getVervangt();
        if (planList != null && !planList.isEmpty()) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, VERVANGT_VANUIT);
            planDto.setVervangtVanuitPlannen(vervangSet);
        }

        planList = plan.getRelatiesVanuitExternePlannen().getTenGevolgeVan();
        if (planList != null && !planList.isEmpty()) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, TEN_GEVOLGE_VAN_VANUIT);
            planDto.setTengevolgeVanVanuitPlannen(vervangSet);
        }

        planList = plan.getRelatiesVanuitExternePlannen().getMuteert();
        if (planList != null && !planList.isEmpty()) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, MUTEERT_VANUIT);
            planDto.setMuteertVanuitPlannen(vervangSet);
        }

        planList = plan.getRelatiesVanuitExternePlannen().getGebruiktInformatieUit();
        if (planList != null && !planList.isEmpty()) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, GEBRUIKT_INFORMATIE_UIT_VANUIT);
            planDto.setGebruiktInfoUitVanuitPlannen(vervangSet);
        }

        planList = plan.getRelatiesVanuitExternePlannen().getGedeeltelijkeHerzieningVan();
        if (planList != null && !planList.isEmpty()) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, GEDEELTELIJKE_HERZIENING_VANUIT);
            planDto.setGedeeltelijkeHerzieningVanuitPlannen(vervangSet);
        }

        planList = plan.getRelatiesVanuitExternePlannen().getUitTeWerkenIn();
        if (planList != null && !planList.isEmpty()) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, UIT_TE_WERKEN_IN_VANUIT);
            planDto.setUitTeWerkenInVanuitPlannen(vervangSet);
        }

        planList = plan.getRelatiesVanuitExternePlannen().getUitgewerktIn();
        if (planList != null && !planList.isEmpty()) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, UITGEWERKINT_IN_VANUIT);
            planDto.setUitgewerktInVanuitPlannen(vervangSet);
        }
    }

    private void extractNormadressant(Plan plan, PlanDto planDto) {
        List<String> normadressanten = plan.getNormadressant();
        normadressanten.forEach(normadressant -> {
            NormadressantDto normadressantDto;
            Optional<NormadressantDto> optionalNormadressantDto = normadressantRepository.findByNorm(normadressant);
            if (optionalNormadressantDto.isPresent()) {
                normadressantDto = optionalNormadressantDto.get();
            } else { // create new
                normadressantDto = new NormadressantDto();
                normadressantDto.setNorm(normadressant);
            }
            normadressantDto.getPlannen().add(planDto);
            //normadressantDto = normadressantRepository.save(normadressantDto);
            planDto.getNormadressanten().add(normadressantDto);
        });
    }

    private void extractVerwijzingNorm(Plan plan, PlanDto planDto) {
        List<String> verwijzingNormen = plan.getVerwijzingNorm();
        verwijzingNormen.forEach(verwijzingNorm -> {
            VerwijzingNormDto verwijzingNormDto;
            Optional<VerwijzingNormDto> optionalVerwijzingNormDto = verwijzingNormRepository.findByNorm(verwijzingNorm);
            if (optionalVerwijzingNormDto.isPresent()) {
                verwijzingNormDto = optionalVerwijzingNormDto.get();
            } else {
                verwijzingNormDto = new VerwijzingNormDto();
                verwijzingNormDto.setNorm(verwijzingNorm);
            }
            verwijzingNormDto.getPlannen().add(planDto);
            //verwijzingNormDto = verwijzingNormRepository.save(verwijzingNormDto);
            planDto.getVerwijzingnormen().add(verwijzingNormDto);
        });
    }

    /*
    precondition: planDto is persisted without locatienaamDto relations!!!!
    postcondition: LocatienaamDto's are persisted and added to planDto which should be saved lateron
     */
    private void extractLocatieNamen(Plan plan,  PlanDto planDto) {
        List<String> orgLocaties = plan.getLocatienamen();
        orgLocaties.forEach(locatie -> {
            LocatieNaamDto locatieNaamDto;
            Optional<LocatieNaamDto> optionalLocNaam = locatieNaamRepository.findByNaam(locatie);
            if (optionalLocNaam.isPresent()) { // use existing locatienaam
                locatieNaamDto = optionalLocNaam.get();
            } else { // create new locatienaam
                locatieNaamDto = new LocatieNaamDto();
                locatieNaamDto.setNaam(locatie);
            }
            locatieNaamDto.getPlannen().add(planDto);
            //locatieNaamDto = locatieNaamRepository.save(locatieNaamDto);
            planDto.getLocaties().add(locatieNaamDto);
        });
    }

    private void extractOndergrond(Plan plan, PlanDto planDto) {
        List<PlanOndergrondenInner> ondergronden = plan.getOndergronden();
        ondergronden.forEach(ondergrond -> {
            OndergrondDto ondergrondDto;
            String ondergrondType = ondergrond.getType().isPresent() ? ondergrond.getType().get() : null;
            String ondergrondDatum = ondergrond.getDatum().isPresent() ? ondergrond.getDatum().get() : null;
            Optional<OndergrondDto> optionalOndergrondDto = ondergrondRepository.findByTypeAndDatum(ondergrondType, ondergrondDatum);
            if (optionalOndergrondDto.isPresent()) {
                ondergrondDto = optionalOndergrondDto.get();
            } else {
                ondergrondDto = new OndergrondDto();
                ondergrondDto.setType(ondergrondType);
                ondergrondDto.setDatum(ondergrondDatum);
            }
            ondergrondDto.getPlannen().add(planDto);
            //ondergrondDto = ondergrondRepository.save(ondergrondDto);
            planDto.getOndergronden().add(ondergrondDto);
        });
    }

    private void extractIllustraties(Plan plan, PlanDto planDto) {
        List<IllustratieReferentie> illustratieReferenties = plan.getIllustraties();
        illustratieReferenties.forEach(illustratieReferentie -> {
            IllustratieDto illustratieDto;
            String href = illustratieReferentie.getHref();
            String type = illustratieReferentie.getType();
            String naam = illustratieReferentie.getNaam().isPresent() ? illustratieReferentie.getNaam().get() : null;
            String legenda = illustratieReferentie.getLegendanaam().isPresent() ? illustratieReferentie.getLegendanaam().get(): null;
            Optional<IllustratieDto> optionalIllustratieDto = illustratieRepository.findByHrefAndTypeAndNaamAndLegendanaam(href, type, naam, legenda);
            if (optionalIllustratieDto.isPresent()) {
                illustratieDto = optionalIllustratieDto.get();
            } else {
                illustratieDto = new IllustratieDto();
                illustratieDto.setHref(href);
                illustratieDto.setType(type);
                illustratieDto.setNaam(naam);
                illustratieDto.setLegendanaam(legenda);
            }
            illustratieDto.setPlan(planDto);
            //illustratieDto = illustratieRepository.save(illustratieDto);
            planDto.getIllustraties().add(illustratieDto);
        });
    }

    private Set<ExternPlanDto> findPlanRef(List<RelatieMetExternPlanReferentie> vervangtList, PlanDto plan, String field) {
        Set<ExternPlanDto> planSet = new HashSet<>();

        vervangtList.forEach(element -> {
            try {
                ExternPlanDto usedExternPlan = externPlanMapper.toExternPlan(element);

                Optional<ExternPlanDto> optionalExternPlanDto = externalPlanRepository.findByNaamAndIdentificatieAndPlanstatusAndPlanstatusdateAndDossierAndHref(usedExternPlan.getNaam(), usedExternPlan.getIdentificatie(), usedExternPlan.getPlanstatus(), usedExternPlan.getPlanstatusdate(), usedExternPlan.getDossier(), usedExternPlan.getHref());
                ExternPlanDto externPlan;
                if (optionalExternPlanDto.isPresent()) {
                    log.debug("Found externalplan: {}", optionalExternPlanDto.get());
                    externPlan = optionalExternPlanDto.get();
                    updatePlan(plan, field, externPlan);

                } else {
                    externPlan = new ExternPlanDto();
                    externPlan.setIdentificatie(usedExternPlan.getIdentificatie());
                    externPlan.setNaam(usedExternPlan.getNaam());
                    externPlan.setPlanstatus(usedExternPlan.getPlanstatus());
                    externPlan.setPlanstatusdate(usedExternPlan.getPlanstatusdate());
                    externPlan.setDossier(usedExternPlan.getDossier());
                    externPlan.setHref(usedExternPlan.getHref());
                    updatePlan(plan, field, externPlan);
                    log.debug("New externalplan: {}", externPlan);
                    externPlan = externalPlanRepository.save(externPlan);
                }
                planSet.add(externPlan);
            } catch (Exception e) {
                log.error("Error converting current planref: {}, plan: {}, field: {}", element, plan, field);
            }
        });
        return planSet;
    }

    private Plan getPlan(String identificatie) {
        Plan plan = null;
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + identificatie);
        String[] expand = {"geometrie", "bbox"};
        for (String param : expand) {
            uriComponentsBuilder.queryParam("expand", param);
        }
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        try {
            plan = APIService.getDirectly(uriComponentsBuilder.build().toUri(), Plan.class);
        } catch (NotFoundException nf) {
            log.error("Plan: {} not found, error: {}", identificatie, nf.toString());
        } catch (Exception e) {
            log.error("Plan: {} error: {}", identificatie, e.toString());
        }
        return plan;
    }

    private void procesPlan(ImroLoadDto imroPlan, UpdateCounter updateCounter) {
        Plan plan;

        try {
            plan = getPlan(imroPlan.getIdentificatie());
            if (plan != null) {
                PlanDto savedPlan = addPlan(plan, imroPlan, updateCounter);
                if (savedPlan != null) {
                    log.trace("Saved plan: {}", savedPlan.toString());
                    imroPlan.setLoaded(true);
                    imroLoadRepository.save(imroPlan);
                }
            }
        } catch (Exception e) {
            log.error("Plan {} niet gevonden. {}", imroPlan.getIdentificatie(), e);
        }
    }

    public UpdateCounter loadPlannen() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByIdentificatieNotLoaded();

        imroLoadDtos.forEach(
                imroPlan -> procesPlan(imroPlan, updateCounter)
        );
        return updateCounter;
    }


    public UpdateCounter loadPlan(String identificatie) {
        UpdateCounter updateCounter = new UpdateCounter();
        if (identificatie != null) {
            Optional<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByIdentificatie(identificatie);

            if (imroLoadDtos.isPresent()) {

                ImroLoadDto imroPlan = imroLoadDtos.get();

                procesPlan(imroPlan, updateCounter);
            }
        }
        return updateCounter;
    }
}
