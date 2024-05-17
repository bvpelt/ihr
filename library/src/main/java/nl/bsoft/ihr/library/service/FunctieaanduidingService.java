package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Functieaanduiding;
import nl.bsoft.ihr.generated.model.FunctieaanduidingCollectie;
import nl.bsoft.ihr.library.mapper.FunctieaanduidingMapper;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.model.dto.AuditLogDto;
import nl.bsoft.ihr.library.model.dto.FunctieaanduidingDto;
import nl.bsoft.ihr.library.model.dto.ImroLoadDto;
import nl.bsoft.ihr.library.model.dto.LocatieDto;
import nl.bsoft.ihr.library.repository.AuditLogRepository;
import nl.bsoft.ihr.library.repository.FunctieaanduidingRepository;
import nl.bsoft.ihr.library.repository.ImroLoadRepository;
import nl.bsoft.ihr.library.repository.LocatieRepository;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class FunctieaanduidingService {
    private final APIService APIService;
    private final ImroLoadRepository imroLoadRepository;
    private final FunctieaanduidingRepository functieaanduidingRepository;
    private final LocatieRepository locatieRepository;
    private final AuditLogRepository auditLogRepository;
    private final FunctieaanduidingMapper functieaanduidingMapper;
    private final LocatieMapper locatieMapper;
    private final int MAXFUNCTIEAANDUIDINGEN = 100;

    @Autowired
    public FunctieaanduidingService(APIService APIService,
                                    ImroLoadRepository imroLoadRepository,
                                    FunctieaanduidingRepository functieaanduidingRepository,
                                    LocatieRepository locatieRepository,
                                    AuditLogRepository auditLogRepository,
                                    FunctieaanduidingMapper functieaanduidingMapper,
                                    LocatieMapper locatieMapper) {
        this.APIService = APIService;
        this.imroLoadRepository = imroLoadRepository;
        this.functieaanduidingRepository = functieaanduidingRepository;
        this.locatieRepository = locatieRepository;
        this.auditLogRepository = auditLogRepository;
        this.functieaanduidingMapper = functieaanduidingMapper;
        this.locatieMapper = locatieMapper;
    }

    public UpdateCounter loadTekstenFromList() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByFiguurNotTried();

        imroLoadDtos.forEach(
                imroPlan -> {
                    procesFunctieaanduidingen(imroPlan.getIdentificatie(), 1, updateCounter, imroPlan);
                    imroLoadRepository.save(imroPlan);
                }
        );
        return updateCounter;
    }

    public void procesFunctieaanduidingen(String planidentificatie, int page, UpdateCounter updateCounter, ImroLoadDto imroPlan) {
        FunctieaanduidingCollectie functieaanduidingen = getFunctieaanduidingForId(planidentificatie, page);
        if (functieaanduidingen != null) {
            saveFunctieaanduidingen(planidentificatie, page, functieaanduidingen, updateCounter, imroPlan);
            if (imroPlan != null) {
                imroPlan.setFunctieaanduidingloaded(true);
                imroPlan.setFiguurloadedprocesed(updateCounter.getProcessed());
            }
        }
        if (imroPlan != null) {
            imroPlan.setFunctieaanduidingtried(true);
        }
    }

    private FunctieaanduidingCollectie getFunctieaanduidingForId(String planidentificatie, int page) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/functieaanduidingen");
        uriComponentsBuilder.queryParam("pageSize", MAXFUNCTIEAANDUIDINGEN);
        uriComponentsBuilder.queryParam("page", page);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), FunctieaanduidingCollectie.class);
    }

    private void saveFunctieaanduidingen(String planidentificatie, int page, FunctieaanduidingCollectie functieaanduidingCollectie, UpdateCounter updateCounter, ImroLoadDto imroPlan) {
        if (functieaanduidingCollectie != null) {
            if (functieaanduidingCollectie.getEmbedded() != null) {
                if (functieaanduidingCollectie.getEmbedded().getFunctieaanduidingen() != null) {
                    // add each found functieaanduiding
                    functieaanduidingCollectie.getEmbedded().getFunctieaanduidingen().forEach(functieaanduiding -> {
                        addFunctieaanduiding(planidentificatie, functieaanduiding, updateCounter);
                    });
                    // while maximum number of functieaanduidingen retrieved, get next page
                    if (functieaanduidingCollectie.getEmbedded().getFunctieaanduidingen().size() == MAXFUNCTIEAANDUIDINGEN) {
                        procesFunctieaanduidingen(planidentificatie, page + 1, updateCounter, imroPlan);
                    }
                }
            }
        }
    }

    @Transactional
    protected FunctieaanduidingDto addFunctieaanduiding(String planidentificatie, Functieaanduiding functieaanduiding, UpdateCounter updateCounter) {
        FunctieaanduidingDto savedFunctieaanduiding = null;

        try {
            String actie = "";
            FunctieaanduidingDto current = functieaanduidingMapper.toFunctieaanduiding(functieaanduiding);
            current.setPlanidentificatie(planidentificatie);
            String md5hash = null;

            if (functieaanduiding.getGeometrie() != null) {
                md5hash = DigestUtils.md5Hex(functieaanduiding.getGeometrie().toString().toUpperCase());
                current.setMd5hash(md5hash);

                Optional<LocatieDto> optionalLocatieDto = locatieRepository.findByMd5hash(md5hash);
                if (!optionalLocatieDto.isPresent()) {
                    LocatieDto locatieDto = locatieMapper.toLocatieDto(functieaanduiding);
                    locatieDto.setMd5hash(md5hash);
                    locatieDto.setRegistratie(LocalDateTime.now());
                    locatieRepository.save(locatieDto);
                    log.debug("Added locatie: {}", md5hash);
                }
            }
            Optional<FunctieaanduidingDto> optionalFound = functieaanduidingRepository.findByPlanidentificatieAndIdentificatie(current.getPlanidentificatie(), current.getIdentificatie());

            if (optionalFound.isPresent()) { // existing entry
                FunctieaanduidingDto found = optionalFound.get();
                if (found.equals(current)) { // not changed
                    actie = "skipped";
                    savedFunctieaanduiding = found;
                    updateCounter.skipped();
                } else {                     // changed
                    actie = "changed";
                    found.setNaam(current.getNaam());
                    found.setStyleid(current.getStyleid());
                    found.setMd5hash(md5hash);
                    savedFunctieaanduiding = functieaanduidingRepository.save(found);
                    updateCounter.updated();
                }
            } else { // new entry
                actie = "add";
                savedFunctieaanduiding = functieaanduidingRepository.save(current);
                updateCounter.add();
            }
            AuditLogDto auditLogDto = new AuditLogDto(planidentificatie, savedFunctieaanduiding.getIdentificatie(), "functieaanduiding", actie);
            auditLogRepository.save(auditLogDto);
        } catch (Exception e) {
            updateCounter.skipped();
            log.error("Error while processing: {} in functiaanduiding processing: {}", functieaanduiding, e);
        }
        return savedFunctieaanduiding;
    }
}
