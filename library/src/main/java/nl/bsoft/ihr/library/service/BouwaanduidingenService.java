package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Bouwaanduiding;
import nl.bsoft.ihr.generated.model.BouwaanduidingCollectie;
import nl.bsoft.ihr.library.mapper.BouwaanduidingMapper;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.model.dto.AuditLogDto;
import nl.bsoft.ihr.library.model.dto.BouwaanduidingDto;
import nl.bsoft.ihr.library.model.dto.ImroLoadDto;
import nl.bsoft.ihr.library.model.dto.LocatieDto;
import nl.bsoft.ihr.library.repository.AuditLogRepository;
import nl.bsoft.ihr.library.repository.BouwaanduidingRepository;
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
public class BouwaanduidingenService {
    private final APIService APIService;
    private final ImroLoadRepository imroLoadRepository;
    private final BouwaanduidingRepository bouwaanduidingRepository;
    private final LocatieRepository locatieRepository;
    private final AuditLogRepository auditLogRepository;
    private final BouwaanduidingMapper bouwaanduidingMapper;
    private final LocatieMapper locatieMapper;
    private final int MAXBOUWAANDUIDINGEN = 100;

    @Autowired
    public BouwaanduidingenService(APIService APIService,
                                   ImroLoadRepository imroLoadRepository,
                                   BouwaanduidingRepository bouwaanduidingRepository,
                                   LocatieRepository locatieRepository,
                                   AuditLogRepository auditLogRepository,
                                   BouwaanduidingMapper bouwaanduidingMapper,
                                   LocatieMapper locatieMapper) {
        this.APIService = APIService;
        this.imroLoadRepository = imroLoadRepository;
        this.bouwaanduidingRepository = bouwaanduidingRepository;
        this.locatieRepository = locatieRepository;
        this.auditLogRepository = auditLogRepository;
        this.bouwaanduidingMapper = bouwaanduidingMapper;
        this.locatieMapper = locatieMapper;
    }

    public UpdateCounter loadBouwaanduidingenFromList() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByBouwaanduidingNotTried();

        imroLoadDtos.forEach(
                imroPlan -> {
                    procesBouwaanduidingen(imroPlan.getIdentificatie(), 1, updateCounter, imroPlan);
                    imroLoadRepository.save(imroPlan);
                }
        );
        return updateCounter;
    }

    public void procesBouwaanduidingen(String planidentificatie, int page, UpdateCounter updateCounter, ImroLoadDto imroPlan) {
        BouwaanduidingCollectie bouwaanduidingen = getBouwaanduidingForId(planidentificatie, page);
        if (bouwaanduidingen != null) {
            saveBouwaanduidingen(planidentificatie, page, bouwaanduidingen, updateCounter, imroPlan);
        }
    }

    private BouwaanduidingCollectie getBouwaanduidingForId(String planidentificatie, int page) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/bouwaanduidingen");
        uriComponentsBuilder.queryParam("pageSize", MAXBOUWAANDUIDINGEN);
        uriComponentsBuilder.queryParam("page", page);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), BouwaanduidingCollectie.class);
    }

    private void saveBouwaanduidingen(String planidentificatie, int page, BouwaanduidingCollectie bouwaanduidingCollectie, UpdateCounter updateCounter, ImroLoadDto imroPlan) {
        if (bouwaanduidingCollectie != null) {
            if (bouwaanduidingCollectie.getEmbedded() != null) {
                if (bouwaanduidingCollectie.getEmbedded().getBouwaanduidingen() != null) {
                    // add each found bouwaanduiding
                    bouwaanduidingCollectie.getEmbedded().getBouwaanduidingen().forEach(bouwaanduiding -> {
                        addBouwaanduiding(planidentificatie, bouwaanduiding, updateCounter);
                    });
                    // while maximum number of bouwaanduidingen retrieved, get next page
                    if (bouwaanduidingCollectie.getEmbedded().getBouwaanduidingen().size() == MAXBOUWAANDUIDINGEN) {
                        procesBouwaanduidingen(planidentificatie, page + 1, updateCounter, imroPlan);
                    }
                    if (imroPlan != null) {
                        imroPlan.setBouwaanduidingloaded(true);
                    }
                }
            }
        }
        if (imroPlan != null) {
            imroPlan.setBouwaanduidingtried(true);
        }
    }

    @Transactional
    protected BouwaanduidingDto addBouwaanduiding(String planidentificatie, Bouwaanduiding bouwaanduiding, UpdateCounter updateCounter) {
        BouwaanduidingDto savedBouwaanduiding = null;

        try {
            String actie = "";
            BouwaanduidingDto current = bouwaanduidingMapper.toBouwaanduiding(bouwaanduiding);
            current.setPlanidentificatie(planidentificatie);
            String md5hash = null;

            if (bouwaanduiding.getGeometrie() != null) {
                md5hash = DigestUtils.md5Hex(bouwaanduiding.getGeometrie().toString().toUpperCase());
                current.setMd5hash(md5hash);

                Optional<LocatieDto> optionalLocatieDto = locatieRepository.findByMd5hash(md5hash);
                if (!optionalLocatieDto.isPresent()) {
                    LocatieDto locatieDto = locatieMapper.toLocatieDto(bouwaanduiding);
                    locatieDto.setMd5hash(md5hash);
                    locatieDto.setRegistratie(LocalDateTime.now());
                    locatieRepository.save(locatieDto);
                    log.debug("Added locatie: {}", md5hash);
                }
            }

            String naam = bouwaanduiding.getNaam();
            String labelInfo = bouwaanduiding.getLabelInfo().isPresent() ? bouwaanduiding.getLabelInfo().get() : null;
            String verwijzingnaartekst = bouwaanduiding.getVerwijzingNaarTekst().isPresent() ? bouwaanduiding.getVerwijzingNaarTekst().get() : null;
            String styleid = bouwaanduiding.getStyleId().isPresent() ? bouwaanduiding.getStyleId().get() : null;

            Optional<BouwaanduidingDto> optionalFound = bouwaanduidingRepository.findByPlanidentificatieAndIdentificatie(current.getPlanidentificatie(), current.getIdentificatie());

            if (optionalFound.isPresent()) { // existing entry
                BouwaanduidingDto found = optionalFound.get();
                if (found.equals(current)) { // not changed
                    actie = "skipped";
                    savedBouwaanduiding = found;
                    updateCounter.skipped();
                } else {                     // changed
                    actie = "changed";
                    found.setNaam(naam);
                    found.setLabelinfo(labelInfo);
                    found.setVerwijzingnaartekst(verwijzingnaartekst);
                    found.setStyleid(styleid);
                    found.setMd5hash(md5hash);
                    savedBouwaanduiding = bouwaanduidingRepository.save(found);
                    updateCounter.updated();
                }
            } else { // new entry
                actie = "add";
                savedBouwaanduiding = bouwaanduidingRepository.save(current);
                updateCounter.add();
            }
            AuditLogDto auditLogDto = new AuditLogDto(planidentificatie, savedBouwaanduiding.getIdentificatie(), "bouwaanduiding", actie);
            auditLogRepository.save(auditLogDto);
        } catch (Exception e) {
            updateCounter.skipped();
            log.error("Error while processing: {} in bouwaanduiding processing: {}", bouwaanduiding, e);
        }
        return savedBouwaanduiding;
    }
}
