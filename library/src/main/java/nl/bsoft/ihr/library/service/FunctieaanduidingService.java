package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Bouwvlak;
import nl.bsoft.ihr.generated.model.Functieaanduiding;
import nl.bsoft.ihr.generated.model.FunctieaanduidingCollectie;
import nl.bsoft.ihr.library.mapper.BouwvlakMapper;
import nl.bsoft.ihr.library.mapper.FunctieaanduidingMapper;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.model.dto.BouwvlakDto;
import nl.bsoft.ihr.library.model.dto.FunctieaanduidingDto;
import nl.bsoft.ihr.library.model.dto.ImroLoadDto;
import nl.bsoft.ihr.library.model.dto.LocatieDto;
import nl.bsoft.ihr.library.repository.BouwvlakRepository;
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
    private final FunctieaanduidingMapper functieaanduidingMapper;
    private final LocatieMapper locatieMapper;
    private final int MAXFUNCTIEAANDUIDINGEN = 100;

    @Autowired
    public FunctieaanduidingService(APIService APIService,
                                    ImroLoadRepository imroLoadRepository,
                                    FunctieaanduidingRepository functieaanduidingRepository,
                                    LocatieRepository locatieRepository,
                                    FunctieaanduidingMapper functieaanduidingMapper,
                                    LocatieMapper locatieMapper) {
        this.APIService = APIService;
        this.imroLoadRepository = imroLoadRepository;
        this.functieaanduidingRepository = functieaanduidingRepository;
        this.locatieRepository = locatieRepository;
        this.functieaanduidingMapper = functieaanduidingMapper;
        this.locatieMapper = locatieMapper;
    }

    public UpdateCounter loadTekstenFromList() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByIdentificatieNotLoaded();

        imroLoadDtos.forEach(
                imroPlan -> {
                    procesFunctieaanduiding(imroPlan.getIdentificatie(), 1, updateCounter);
                }
        );
        return updateCounter;
    }

    public void procesFunctieaanduiding(String planidentificatie, int page, UpdateCounter updateCounter) {
        FunctieaanduidingCollectie functieaanduidingen = getFunctieaanduidingForId(planidentificatie, page);
        if (functieaanduidingen != null) {
            saveFunctieaanduidingen(planidentificatie, page, functieaanduidingen, updateCounter);
        }
    }

    private FunctieaanduidingCollectie getFunctieaanduidingForId(String planidentificatie, int page) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/functieaanduidingen");
        uriComponentsBuilder.queryParam("pageSize", MAXFUNCTIEAANDUIDINGEN);
        uriComponentsBuilder.queryParam("page", page);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), FunctieaanduidingCollectie.class);
    }

    private void saveFunctieaanduidingen(String planidentificatie, int page, FunctieaanduidingCollectie functieaanduidingCollectie, UpdateCounter updateCounter) {
        if (functieaanduidingCollectie != null) {
            if (functieaanduidingCollectie.getEmbedded() != null) {
                if (functieaanduidingCollectie.getEmbedded().getFunctieaanduidingen() != null) {
                    // add each found functieaanduiding
                    functieaanduidingCollectie.getEmbedded().getFunctieaanduidingen().forEach(functieaanduiding -> {
                        addFunctieaanduiding(planidentificatie, functieaanduiding, updateCounter);
                    });
                    // while maximum number of functieaanduidingen retrieved, get next page
                    if (functieaanduidingCollectie.getEmbedded().getFunctieaanduidingen().size() == MAXFUNCTIEAANDUIDINGEN) {
                        procesFunctieaanduiding(planidentificatie, page + 1, updateCounter);
                    }
                }
            }
        }
    }

    @Transactional
    protected FunctieaanduidingDto addFunctieaanduiding(String planidentificatie, Functieaanduiding functieaanduiding, UpdateCounter updateCounter) {
        FunctieaanduidingDto savedFunctieaanduiding = null;

        try {
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
                    savedFunctieaanduiding = found;
                    updateCounter.skipped();
                } else {                     // changed
                    found.setNaam(current.getNaam());
                    found.setStyleid(current.getStyleid());
                    found.setMd5hash(md5hash);
                    savedFunctieaanduiding = functieaanduidingRepository.save(found);
                    updateCounter.updated();
                }
            } else { // new entry
                savedFunctieaanduiding = functieaanduidingRepository.save(current);
                updateCounter.add();
            }
        } catch (Exception e) {
            updateCounter.skipped();
            log.error("Error while processing: {} in functiaanduiding processing: {}", functieaanduiding, e);
        }
        return savedFunctieaanduiding;
    }
}
