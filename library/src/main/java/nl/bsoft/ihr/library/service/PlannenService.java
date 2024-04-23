package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.*;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.mapper.PlanMapper;
import nl.bsoft.ihr.library.model.dto.*;
import nl.bsoft.ihr.library.repository.*;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.apache.commons.codec.digest.DigestUtils;
import org.geolatte.geom.V;
import org.locationtech.jts.io.ParseException;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private final PlanStatusRepository planStatusRepository;
    private final OverheidRepository overheidRepository;
    private final VerwijzingNormRepository verwijzingNormRepository;
    private final NormadressantRepository normadressantRepository;
    private final OndergrondRepository ondergrondRepository;
    private final ExternalPlanRepository externalPlanRepository;
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
                          LocatieMapper locatieMapper,
                          PlanStatusRepository planStatusRepository,
                          VerwijzingNormRepository verwijzingNormRepository,
                          NormadressantRepository normadressantRepository,
                          OndergrondRepository ondergrondRepository,
                          ExternalPlanRepository externalPlanRepository
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
        this.planStatusRepository = planStatusRepository;
        this.verwijzingNormRepository = verwijzingNormRepository;
        this.normadressantRepository = normadressantRepository;
        this.ondergrondRepository = ondergrondRepository;
        this.externalPlanRepository = externalPlanRepository;
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

            extractLocation(plan, md5hash);

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

            // onetomany relations
            extractPlanStatus(plan, planDto);

            extractBeleidsmatigeOverheid(plan, planDto);

            extractPublicerendeOverheid(plan, planDto);



            planRepository.save(planDto); // reference for locatienamen

            extractRelatiesMetExternePlannen(plan, planDto);

            extractRelatiesVanuitExternePlannen(plan, planDto);

            // manytomany relations
            extractNormadressant(plan, planDto);

            extractVerwijzingNorm(plan, planDto);

            extractLocatieNamen(plan, planDto);

            extractOndergrond(plan, planDto);

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

    private void extractLocatieNamen(Plan plan, PlanDto planDto) {
        List<String> locatieNaamDtoSet = plan.getLocatienamen();
        Iterator<String> locatieNaamIterator = locatieNaamDtoSet.iterator();
        while (locatieNaamIterator.hasNext()) {
            String puLocatieNaam = locatieNaamIterator.next();

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
    }

    private void extractOndergrond(Plan plan, PlanDto planDto) {
        List<PlanOndergrondenInner> ondergronden = plan.getOndergronden();
        Iterator<PlanOndergrondenInner> ondergrondenIterator = ondergronden.iterator();
        while (ondergrondenIterator.hasNext()) {
            PlanOndergrondenInner planOndergrondenInner = ondergrondenIterator.next();
            String type = planOndergrondenInner.getType().isPresent()? planOndergrondenInner.getType().get(): null;
            String datum = planOndergrondenInner.getDatum().isPresent()? planOndergrondenInner.getDatum().get(): null;
            Optional<OndergrondDto> optionalOndergrondDto = ondergrondRepository.findByTypeAndDatum(type, datum);
            OndergrondDto current = null;
            if (optionalOndergrondDto.isPresent()) {
                current = optionalOndergrondDto.get();
            } else {
                current = new OndergrondDto();
                current.setType(type);
                current.setDatum(datum);
            }
            current.getPlannen().add(planDto);
            current = ondergrondRepository.save(current);
            planDto.getOndergronden().add(current);
        }
    }
    private void extractVerwijzingNorm(Plan plan, PlanDto planDto) {
        List<String> verwijzingNormen = plan.getVerwijzingNorm();
        Iterator<String> verwijzingNormIterable = verwijzingNormen.iterator();
        while (verwijzingNormIterable.hasNext()) {
            String verwijzing = verwijzingNormIterable.next();
            Optional<VerwijzingNormDto> optionalVerwijzingNormDto = verwijzingNormRepository.findByNorm(verwijzing);
            VerwijzingNormDto current = null;
            if (optionalVerwijzingNormDto.isPresent()) {
                current = optionalVerwijzingNormDto.get();
            } else {
                current = new VerwijzingNormDto();
                current.setNorm(verwijzing);
            }
            current.getPlannen().add(planDto);
            current = verwijzingNormRepository.save(current);
            planDto.getVerwijzingnormen().add(current);
        }
    }

    public void extractNormadressant(Plan plan, PlanDto planDto) {
        List<String> normadressanten = plan.getNormadressant();
        Iterator<String> normadressantIterator = normadressanten.iterator();
        while (normadressantIterator.hasNext()) {
            String normadressant = normadressantIterator.next();
            Optional<NormadressantDto> optionalNormadressantDto = normadressantRepository.findByNorm(normadressant);
            NormadressantDto current = null;
            if (optionalNormadressantDto.isPresent()) {
                current = optionalNormadressantDto.get();
            } else {
                current = new NormadressantDto();
                current.setNorm(normadressant);
            }
            current.getPlannen().add(planDto);
            current = normadressantRepository.save(current);
            planDto.getNormadressanten().add(current);
        }
    }

    private void extractPublicerendeOverheid(Plan plan, PlanDto planDto) {
        JsonNullable<PlanPublicerendBevoegdGezag> puOverheidDto = plan.getPublicerendBevoegdGezag();

        if (puOverheidDto.isPresent()) {
            if (puOverheidDto.get().getCode().isPresent()) {
                Optional<OverheidDto> optionalPublicerendeOverheid = overheidRepository.findByCode(puOverheidDto.get().getCode().get());
                OverheidDto currentPublicerendeOverheid = null;
                if (optionalPublicerendeOverheid.isPresent()) {
                    currentPublicerendeOverheid = optionalPublicerendeOverheid.get();
                } else {
                    currentPublicerendeOverheid = new OverheidDto();
                    currentPublicerendeOverheid.setNaam(puOverheidDto.get().getNaam().get());
                    currentPublicerendeOverheid.setCode(puOverheidDto.get().getCode().get());
                    currentPublicerendeOverheid.setType(puOverheidDto.get().getType().getValue());
                }
                currentPublicerendeOverheid.getBeleidsmatig().add(planDto);
                currentPublicerendeOverheid = overheidRepository.save(currentPublicerendeOverheid);

                planDto.setPublicerendeoverheid(currentPublicerendeOverheid);
                log.debug("publicerende overheid: {}", currentPublicerendeOverheid);
            }
        }
    }
    private void extractBeleidsmatigeOverheid(Plan plan, PlanDto planDto) {
        PlanBeleidsmatigVerantwoordelijkeOverheid beleidsmatigeOverheid = plan.getBeleidsmatigVerantwoordelijkeOverheid();

        if (beleidsmatigeOverheid.getCode().isPresent()) {
            Optional<OverheidDto> OptionalBeleidsmatigOverheid = overheidRepository.findByCode(beleidsmatigeOverheid.getCode().get());
            OverheidDto currentBeleidsMatigeOverheid = null;
            if (OptionalBeleidsmatigOverheid.isPresent()) {
                currentBeleidsMatigeOverheid = OptionalBeleidsmatigOverheid.get();
            } else {
                currentBeleidsMatigeOverheid = new OverheidDto();
                currentBeleidsMatigeOverheid.setNaam(beleidsmatigeOverheid.getNaam().get());
                currentBeleidsMatigeOverheid.setCode(beleidsmatigeOverheid.getCode().get());
                currentBeleidsMatigeOverheid.setType(beleidsmatigeOverheid.getType().getValue());
            }
            currentBeleidsMatigeOverheid.getBeleidsmatig().add(planDto);
            currentBeleidsMatigeOverheid = overheidRepository.save(currentBeleidsMatigeOverheid);
            planDto.setBeleidsmatigeoverheid(currentBeleidsMatigeOverheid);
            log.debug("beleidsmatige overheid: {}", currentBeleidsMatigeOverheid);
        }
    }
    Set<ExternPlanDto> findPlanRef(List<RelatieMetExternPlanReferentie>  vervangtList, PlanDto plan, String field) {
        Set<ExternPlanDto> planSet = new HashSet<>();

        vervangtList.forEach(element -> {
            String naam = element.getNaam().isPresent() ? element.getNaam().get() : null;
            String identificatie = element.getId().isPresent() ? element.getId().get() : null;
            String planstatus = element.getPlanstatusInfo().isPresent() ? element.getPlanstatusInfo().get().getPlanstatus() :null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate planstatusdate = element.getPlanstatusInfo().isPresent() ? LocalDate.parse(element.getPlanstatusInfo().get().getDatum(), formatter) : null;
            String dossier = element.getDossier().isPresent() ? element.getDossier().get().getStatus() : null;
            String href = element.getHref().isPresent() ? element.getHref().get() : null;

            Optional<ExternPlanDto> optionalExternPlanDto = externalPlanRepository.findByNaamAndIdentificatieAndPlanstatusAndPlanstatusdateAndDossierAndHref(naam, identificatie, planstatus, planstatusdate, dossier, href);
            ExternPlanDto currentPlan = null;
            if (optionalExternPlanDto.isPresent()) {
                log.debug("Found externalplan: {}", optionalExternPlanDto.get());
                currentPlan = optionalExternPlanDto.get();
                updatePlan(plan, field, currentPlan);

            } else {
                currentPlan = new ExternPlanDto();
                currentPlan.setIdentificatie(identificatie);
                currentPlan.setNaam(naam);
                currentPlan.setPlanstatus(planstatus);
                currentPlan.setPlanstatusdate(planstatusdate);
                currentPlan.setDossier(dossier);
                currentPlan.setHref(href);
                updatePlan(plan, field, currentPlan);
                log.debug("New externalplan: {}", currentPlan);
                currentPlan = externalPlanRepository.save(currentPlan);
            }
            planSet.add(currentPlan);
        });
        return planSet;
    }

    private static void updatePlan(PlanDto plan, String field, ExternPlanDto currentPlan) {
        switch (field) {
            case "VervangtMet": {
                if (currentPlan.getVervangtmetplan() == null) {
                    currentPlan.setVervangtmetplan(plan);
                } else {
                    log.error("Vervangtmetplan heeft al een waarde voor externplan {}", currentPlan.getIdentificatie());
                }
            }
            break;
            case "TengevolgeVanMet": {
                if (currentPlan.getTengevolgevanmetplan() == null) {
                    currentPlan.setTengevolgevanmetplan(plan);
                } else {
                    log.error("Tengevolgevanmetplan heeft al een waarde voor externplan {}", currentPlan.getIdentificatie());
                }
            }
            break;
            case "MuteertMet": {
                if (currentPlan.getMuteertmetplan() == null) {
                    currentPlan.setMuteertmetplan(plan);
                } else {
                    log.error("Muteertmetplan heeft al een waarde voor externplan {}", currentPlan.getIdentificatie());
                }
            }
            break;
            case "GebruiktInformatieUitMet": {
                if (currentPlan.getGebruiktinfouitmetplan() == null) {
                    currentPlan.setGebruiktinfouitmetplan(plan);
                } else {
                    log.error("Gebruiktinfouitmetplan heeft al een waarde voor externplan {}", currentPlan.getIdentificatie());
                }
            }
            break;
            case "GedeeltelijkeHerzieningMet": {
                if (currentPlan.getGedeeltelijkeherzieningmetplan() == null) {
                    currentPlan.setGedeeltelijkeherzieningmetplan(plan);
                } else {
                    log.error("Gedeeltelijkeherzieningmetplan heeft al een waarde voor externplan {}", currentPlan.getIdentificatie());
                }
            }
            break;
            case "UitTeWerkenInMet": {
                if (currentPlan.getUittewerkinginmetplan() == null) {
                    currentPlan.setUittewerkinginmetplan(plan);
                } else {
                    log.error("Uittewerkinginmetplan heeft al een waarde voor externplan {}", currentPlan.getIdentificatie());
                }
            }
            break;
            case "UitgewerktInMet": {
                if (currentPlan.getUitgewerktinmetplan() == null) {
                    currentPlan.setUitgewerktinmetplan(plan);
                } else {
                    log.error("Uitgewerktinmetplan heeft al een waarde voor externplan {}", currentPlan.getIdentificatie());
                }
            }
            break;
            case "VervangtVanuit": {
                if (currentPlan.getVervangtvanuitplan() == null) {
                    currentPlan.setVervangtvanuitplan(plan);
                } else {
                    log.error("Vervangtvanuitplan heeft al een waarde voor externplan {}", currentPlan.getIdentificatie());
                }
            }
            break;
            case "TenGevolgeVanVanuit": {
                if (currentPlan.getTegevolgevanvanuitplan() == null) {
                    currentPlan.setTegevolgevanvanuitplan(plan);
                } else {
                    log.error("Tegevolgevanvanuitplan heeft al een waarde voor externplan {}", currentPlan.getIdentificatie());
                }
            }
            break;
            case "MuteertVanuit": {
                if (currentPlan.getMuteertvanuitplan() == null) {
                    currentPlan.setMuteertvanuitplan(plan);
                } else {
                    log.error("Muteertvanuitplan heeft al een waarde voor externplan {}", currentPlan.getIdentificatie());
                }
            }
            break;
            case "GebruiktInformatieUitVanuit": {
                if (currentPlan.getGebruiktinforuitvanuitplan() == null) {
                    currentPlan.setGebruiktinforuitvanuitplan(plan);
                } else {
                    log.error("Gebruiktinforuitvanuitplan heeft al een waarde voor externplan {}", currentPlan.getIdentificatie());
                }
            }
            break;
            case "GedeeltelijkeHerzieningVanuit": {
                if (currentPlan.getGedeeltelijkeherzieningvanuitplan() == null) {
                    currentPlan.setGedeeltelijkeherzieningvanuitplan(plan);
                } else {
                    log.error("Gedeeltelijkeherzieningvanuitplan heeft al een waarde voor externplan {}", currentPlan.getIdentificatie());
                }
            }
            break;
            case "UitTeWerkenInVanuit": {
                if (currentPlan.getUittewerkinginvanuitplan() == null) {
                    currentPlan.setUittewerkinginvanuitplan(plan);
                } else {
                    log.error("Uittewerkinginvanuitplan heeft al een waarde voor externplan {}", currentPlan.getIdentificatie());
                }
            }
            break;
            case "UitgewerkintInVanuit": {
                if (currentPlan.getUitgewerktinvanuitplan() == null) {
                    currentPlan.setUitgewerktinvanuitplan(plan);
                } else {
                    log.error("Uitgewerktinvanuitplan heeft al een waarde voor externplan {}", currentPlan.getIdentificatie());
                }
            }
            break;
            default: log.error("Field {} not found", field);
        }
    }

    private void extractRelatiesMetExternePlannen(Plan plan, PlanDto planDto) {
        List<RelatieMetExternPlanReferentie> planList = plan.getRelatiesMetExternePlannen().getVervangt();
        if (planList != null && planList.size() > 0) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, "VervangtMet");
            planDto.setVervangtMetPlannen(vervangSet);
        }

        planList = plan.getRelatiesMetExternePlannen().getTenGevolgeVan();
        if (planList != null && planList.size() > 0) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList,planDto, "TengevolgeVanMet");
            planDto.setTengevolgeVanMetPlannen(vervangSet);
        }

        planList = plan.getRelatiesMetExternePlannen().getMuteert();
        if (planList != null && planList.size() > 0) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, "MuteertMet");
            planDto.setMuteertMetPlannen(vervangSet);
        }

        planList = plan.getRelatiesMetExternePlannen().getGebruiktInformatieUit();
        if (planList != null && planList.size() > 0) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, "GebruiktInformatieUitMet");
            planDto.setGebruiktInfoUitMetPlannen(vervangSet);
        }

        planList = plan.getRelatiesMetExternePlannen().getGedeeltelijkeHerzieningVan();
        if (planList != null && planList.size() > 0) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, "GedeeltelijkeHerzieningMet");
            planDto.setGedeeltelijkeHerzieningMetPlannen(vervangSet);
        }

        planList = plan.getRelatiesMetExternePlannen().getUitTeWerkenIn();
        if (planList != null && planList.size() > 0) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, "UitTeWerkenInMet");
            planDto.setUitTeWerkenInMetPlannen(vervangSet);
        }

        planList = plan.getRelatiesMetExternePlannen().getUitgewerktIn();
        if (planList != null && planList.size() > 0) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, "UitgewerktInMet");
            planDto.setUitgewerktInMetPlannen(vervangSet);
        }
    }
    private void extractRelatiesVanuitExternePlannen(Plan plan, PlanDto planDto) {
        List<RelatieMetExternPlanReferentie> planList = plan.getRelatiesVanuitExternePlannen().getVervangt();
        if (planList != null && planList.size() > 0) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, "VervangtVanuit");
            planDto.setVervangtVanuitPlannen(vervangSet);
        }

        planList = plan.getRelatiesVanuitExternePlannen().getTenGevolgeVan();
        if (planList != null && planList.size() > 0) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, "TenGevolgeVanVanuit");
            planDto.setTengevolgeVanVanuitPlannen(vervangSet);
        }

        planList = plan.getRelatiesVanuitExternePlannen().getMuteert();
        if (planList != null && planList.size() > 0) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, "MuteertVanuit");
            planDto.setMuteertVanuitPlannen(vervangSet);
        }

        planList = plan.getRelatiesVanuitExternePlannen().getGebruiktInformatieUit();
        if (planList != null && planList.size() > 0) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, "GebruiktInformatieUitVanuit");
            planDto.setGebruiktInfoUitVanuitPlannen(vervangSet);
        }

        planList = plan.getRelatiesVanuitExternePlannen().getGedeeltelijkeHerzieningVan();
        if (planList != null && planList.size() > 0) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, "GedeeltelijkeHerzieningVanuit");
            planDto.setGedeeltelijkeHerzieningVanuitPlannen(vervangSet);
        }

        planList = plan.getRelatiesVanuitExternePlannen().getUitTeWerkenIn();
        if (planList != null && planList.size() > 0) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, "UitTeWerkenInVanuit");
            planDto.setUitTeWerkenInVanuitPlannen(vervangSet);
        }

        planList = plan.getRelatiesVanuitExternePlannen().getUitgewerktIn();
        if (planList != null && planList.size() > 0) {
            Set<ExternPlanDto> vervangSet = findPlanRef(planList, planDto, "UitgewerkintInVanuit");
            planDto.setUitgewerktInVanuitPlannen(vervangSet);
        }
    }

    private void extractPlanStatus(Plan plan, PlanDto planDto) {
        Optional<PlanStatusDto> optionalPlanStatusDto = planStatusRepository.findByStatusAndDatum(plan.getPlanstatusInfo().getPlanstatus().getValue(), plan.getPlanstatusInfo().getDatum());
        PlanStatusDto currentPlanStatus = null;
        if (optionalPlanStatusDto.isPresent()) {
            currentPlanStatus = optionalPlanStatusDto.get();
        } else {
            currentPlanStatus = new PlanStatusDto();
            currentPlanStatus.setStatus(plan.getPlanstatusInfo().getPlanstatus().getValue());
            currentPlanStatus.setDatum(plan.getPlanstatusInfo().getDatum());
        }
        currentPlanStatus.getPlannen().add(planDto);
        currentPlanStatus = planStatusRepository.save(currentPlanStatus);
        planDto.setPlanstatus(currentPlanStatus);
        log.info("Planstatus: {}", currentPlanStatus.toString());
    }

    private void extractLocation(Plan plan, String md5hash) throws ParseException {
        Optional<LocatieDto> optionalLocatieDto = locatieRepository.findByMd5hash(md5hash);
        if (!optionalLocatieDto.isPresent()) {
            LocatieDto locatieDto = locatieMapper.toLocatieDto(plan);
            locatieDto.setMd5hash(md5hash);
            locatieDto.setRegistratie(LocalDateTime.now());
            locatieRepository.save(locatieDto);
            log.debug("Added locatie: {}", md5hash);
        }
    }

    private PlanDto updatePlanDto(PlanDto original, PlanDto planDto) {
        PlanDto updatedPlan = original;
        original.setPlantype(planDto.getPlantype());
        original.setBeleidsmatigeoverheid(planDto.getBeleidsmatigeoverheid());
        original.setPublicerendeoverheid(planDto.getPublicerendeoverheid());
        original.setNaam(planDto.getNaam());
        original.setLocaties(planDto.getLocaties());
        original.setPlanstatus(planDto.getPlanstatus());
        original.setBesluitnummer(planDto.getBesluitnummer());
        original.setRegelstatus(planDto.getRegelstatus());
        original.setDossierid(planDto.getDossierid());
        original.setDossierstatus(planDto.getDossierstatus());
        original.setIsparapluplan(planDto.getIsparapluplan());
        original.setBeroepenbezwaar(planDto.getBeroepenbezwaar());
        original.setMd5hash(planDto.getMd5hash());

        return original;
    }

    public Plan getPlan(String identificatie) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + identificatie);
        String[] expand = {"geometrie"};
        for (String param : expand) {
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
