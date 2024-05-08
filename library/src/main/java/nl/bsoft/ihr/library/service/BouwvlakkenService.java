package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Bouwvlak;
import nl.bsoft.ihr.generated.model.BouwvlakCollectie;
import nl.bsoft.ihr.library.mapper.BouwvlakMapper;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.model.dto.AuditLogDto;
import nl.bsoft.ihr.library.model.dto.BouwvlakDto;
import nl.bsoft.ihr.library.model.dto.ImroLoadDto;
import nl.bsoft.ihr.library.model.dto.LocatieDto;
import nl.bsoft.ihr.library.repository.AuditLogRepository;
import nl.bsoft.ihr.library.repository.BouwvlakRepository;
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
public class BouwvlakkenService {
    private final APIService APIService;
    private final ImroLoadRepository imroLoadRepository;
    private final BouwvlakRepository bouwvlakRepository;
    private final LocatieRepository locatieRepository;
    private final AuditLogRepository auditLogRepository;
    private final BouwvlakMapper bouwvlakMapper;
    private final LocatieMapper locatieMapper;
    private final int MAXBOUWVLAKKEN = 100;

    @Autowired
    public BouwvlakkenService(APIService APIService,
                              ImroLoadRepository imroLoadRepository,
                              BouwvlakRepository bouwvlakRepository,
                              LocatieRepository locatieRepository,
                              AuditLogRepository auditLogRepository,
                              BouwvlakMapper bouwvlakMapper,
                              LocatieMapper locatieMapper) {
        this.APIService = APIService;
        this.imroLoadRepository = imroLoadRepository;
        this.bouwvlakRepository = bouwvlakRepository;
        this.locatieRepository = locatieRepository;
        this.auditLogRepository = auditLogRepository;
        this.bouwvlakMapper = bouwvlakMapper;
        this.locatieMapper = locatieMapper;
    }

    public UpdateCounter loadBouwvlakkenFromList() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByBouwvlakkenNotTried();

        imroLoadDtos.forEach(
                imroPlan -> {
                    procesBouwvlakken(imroPlan.getIdentificatie(), 1, updateCounter, imroPlan);
                    imroLoadRepository.save(imroPlan);
                }
        );
        return updateCounter;
    }

    public void procesBouwvlakken(String planidentificatie, int page, UpdateCounter updateCounter, ImroLoadDto imroPlan) {
        BouwvlakCollectie bouwvlakCollectie = getBouwvlakkenForId(planidentificatie, page);
        if (bouwvlakCollectie != null) {
            saveBouwvlakken(planidentificatie, page, bouwvlakCollectie, updateCounter, imroPlan);
        }
    }

    private BouwvlakCollectie getBouwvlakkenForId(String planidentificatie, int page) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/bouwvlakken");
        uriComponentsBuilder.queryParam("pageSize", MAXBOUWVLAKKEN);
        uriComponentsBuilder.queryParam("page", page);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), BouwvlakCollectie.class);
    }

    private void saveBouwvlakken(String planidentificatie, int page, BouwvlakCollectie bouwvlakCollectie, UpdateCounter updateCounter, ImroLoadDto imroPlan) {
        if (bouwvlakCollectie != null) {
            if (bouwvlakCollectie.getEmbedded() != null) {
                if (bouwvlakCollectie.getEmbedded().getBouwvlakken() != null) {
                    // add each found bouwvlak
                    bouwvlakCollectie.getEmbedded().getBouwvlakken().forEach(bouwvlak -> {
                        addBouwvlak(planidentificatie, bouwvlak, updateCounter);
                    });
                    // while maximum number of bouwvlaken retrieved, get next page
                    if (bouwvlakCollectie.getEmbedded().getBouwvlakken().size() == MAXBOUWVLAKKEN) {
                        procesBouwvlakken(planidentificatie, page + 1, updateCounter, imroPlan);
                    }
                    if (imroPlan != null) {
                        imroPlan.setBouwvlakkenloaded(true);
                    }
                }
            }
        }
        if (imroPlan != null) {
            imroPlan.setBouwvlakkentried(true);
        }
    }

    @Transactional
    protected BouwvlakDto addBouwvlak(String planidentificatie, Bouwvlak bouwvlak, UpdateCounter updateCounter) {
        BouwvlakDto savedBouwvlak = null;

        try {
            String actie = "";
            BouwvlakDto current = bouwvlakMapper.toBestemmingsvlak(bouwvlak);
            current.setPlanidentificatie(planidentificatie);
            String md5hash = null;

            if (bouwvlak.getGeometrie() != null) {
                md5hash = DigestUtils.md5Hex(bouwvlak.getGeometrie().toString().toUpperCase());
                current.setMd5hash(md5hash);

                Optional<LocatieDto> optionalLocatieDto = locatieRepository.findByMd5hash(md5hash);
                if (!optionalLocatieDto.isPresent()) {
                    LocatieDto locatieDto = locatieMapper.toLocatieDto(bouwvlak);
                    locatieDto.setMd5hash(md5hash);
                    locatieDto.setRegistratie(LocalDateTime.now());
                    locatieRepository.save(locatieDto);
                    log.debug("Added locatie: {}", md5hash);
                }
            }
            Optional<BouwvlakDto> optionalFound = bouwvlakRepository.findByPlanidentificatieAndIdentificatie(current.getPlanidentificatie(), current.getIdentificatie());

            if (optionalFound.isPresent()) { // existing entry
                BouwvlakDto found = optionalFound.get();
                if (found.equals(current)) { // not changed
                    actie = "skipped";
                    savedBouwvlak = found;
                    updateCounter.skipped();
                } else {                     // changed
                    actie = "changed";
                    found.setNaam(current.getNaam());
                    found.setStyleid(current.getStyleid());
                    found.setMd5hash(md5hash);
                    savedBouwvlak = bouwvlakRepository.save(found);
                    updateCounter.updated();
                }
            } else { // new entry
                actie = "add";
                savedBouwvlak = bouwvlakRepository.save(current);
                updateCounter.add();
            }

            AuditLogDto auditLogDto = new AuditLogDto(planidentificatie, savedBouwvlak.getIdentificatie(), "bouwvlak", actie);
            auditLogRepository.save(auditLogDto);
        } catch (Exception e) {
            updateCounter.skipped();
            log.error("Error while processing: {} in bouwvlakken processing: {}", bouwvlak, e);
        }
        return savedBouwvlak;
    }
}
