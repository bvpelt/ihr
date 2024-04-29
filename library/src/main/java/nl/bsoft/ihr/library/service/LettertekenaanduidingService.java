package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.*;
import nl.bsoft.ihr.library.mapper.LettertekenaanduidingMapper;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.model.dto.BouwaanduidingDto;
import nl.bsoft.ihr.library.model.dto.ImroLoadDto;
import nl.bsoft.ihr.library.model.dto.LettertekenaanduidingDto;
import nl.bsoft.ihr.library.model.dto.LocatieDto;
import nl.bsoft.ihr.library.repository.ImroLoadRepository;
import nl.bsoft.ihr.library.repository.LettertekenaanduidingRepository;
import nl.bsoft.ihr.library.repository.LocatieRepository;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LettertekenaanduidingService {
    private final APIService APIService;
    private final ImroLoadRepository imroLoadRepository;
    private final LettertekenaanduidingRepository lettertekenaanduidingRepository;
    private final LocatieRepository locatieRepository;
    private final LettertekenaanduidingMapper lettertekenaanduidingMapper;
    private final LocatieMapper locatieMapper;
    private final int MAXLETTERTEKENAANDUIDINGEN = 100;

    @Autowired
    public LettertekenaanduidingService(APIService APIService,
                                        ImroLoadRepository imroLoadRepository,
                                        LettertekenaanduidingRepository lettertekenaanduidingRepository,
                                        LocatieRepository locatieRepository,
                                        LettertekenaanduidingMapper lettertekenaanduidingMapper,
                                        LocatieMapper locatieMapper) {
        this.APIService = APIService;
        this.imroLoadRepository = imroLoadRepository;
        this.lettertekenaanduidingRepository = lettertekenaanduidingRepository;
        this.locatieRepository = locatieRepository;
        this.lettertekenaanduidingMapper = lettertekenaanduidingMapper;
        this.locatieMapper = locatieMapper;
    }

    public UpdateCounter loadLettertekenaanduidingenFromList() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByIdentificatieNotLoaded();

        imroLoadDtos.forEach(
                imroPlan -> {
                    procesLettertekenaanduiding(imroPlan.getIdentificatie(), 1, updateCounter);
                }
        );
        return updateCounter;
    }

    public void procesLettertekenaanduiding(String planidentificatie, int page, UpdateCounter updateCounter) {
        LettertekenaanduidingCollectie lettertekenaanduidingen = getLettertekenaanduidingForId(planidentificatie, page);
        if (lettertekenaanduidingen != null) {
            saveLettertekenaanduidingen(planidentificatie, page, lettertekenaanduidingen, updateCounter);
        }
    }

    private LettertekenaanduidingCollectie getLettertekenaanduidingForId(String planidentificatie, int page) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/lettertekenaanduidingen");
        uriComponentsBuilder.queryParam("pageSize", MAXLETTERTEKENAANDUIDINGEN);
        uriComponentsBuilder.queryParam("page", page);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), LettertekenaanduidingCollectie.class);
    }

    private void saveLettertekenaanduidingen(String planidentificatie, int page, LettertekenaanduidingCollectie lettertekenaanduidingCollectie, UpdateCounter updateCounter) {
        if (lettertekenaanduidingCollectie != null) {
            if (lettertekenaanduidingCollectie.getEmbedded() != null) {
                if (lettertekenaanduidingCollectie.getEmbedded().getLettertekenaanduidingen() != null) {
                    // add each found bouwaanduiding
                    lettertekenaanduidingCollectie.getEmbedded().getLettertekenaanduidingen().forEach(letteraanduiding -> {
                        addLettertekenaanduiding(planidentificatie, letteraanduiding, updateCounter);
                    });
                    // while maximum number of bouwaanduidingen retrieved, get next page
                    if (lettertekenaanduidingCollectie.getEmbedded().getLettertekenaanduidingen().size() == MAXLETTERTEKENAANDUIDINGEN) {
                        procesLettertekenaanduiding(planidentificatie, page + 1, updateCounter);
                    }
                }
            }
        }
    }

    @Transactional
    protected LettertekenaanduidingDto addLettertekenaanduiding(String planidentificatie, Lettertekenaanduiding lettertekenaanduiding, UpdateCounter updateCounter) {
        LettertekenaanduidingDto savedLettertekenaanduiding = null;

        try {
            LettertekenaanduidingDto current = lettertekenaanduidingMapper.toLettertekenaanduiding(lettertekenaanduiding);
            current.setPlanidentificatie(planidentificatie);
            String md5hash = null;

            if (lettertekenaanduiding.getGeometrie() != null) {
                md5hash = DigestUtils.md5Hex(lettertekenaanduiding.getGeometrie().toString().toUpperCase());
                current.setMd5hash(md5hash);

                Optional<LocatieDto> optionalLocatieDto = locatieRepository.findByMd5hash(md5hash);
                if (!optionalLocatieDto.isPresent()) {
                    LocatieDto locatieDto = locatieMapper.toLocatieDto(lettertekenaanduiding);
                    locatieDto.setMd5hash(md5hash);
                    locatieDto.setRegistratie(LocalDateTime.now());
                    locatieRepository.save(locatieDto);
                    log.debug("Added locatie: {}", md5hash);
                }
            }

            lettertekenaanduiding.getBestemmingsfuncties().forEach(bestemmingsfunctie -> {
                String functie = bestemmingsfunctie.getBestemmingsfunctie();
                String functieniveau = bestemmingsfunctie.getFunctieniveau();
                // [TODO] 
            });


            String naam = lettertekenaanduiding.getNaam();
            String labelInfo = lettertekenaanduiding.getLabelInfo().isPresent() ? lettertekenaanduiding.getLabelInfo().get() : null;
            String styleid = lettertekenaanduiding.getStyleId().isPresent() ? lettertekenaanduiding.getStyleId().get() : null;

            Optional<LettertekenaanduidingDto> optionalFound = lettertekenaanduidingRepository.findByPlanidentificatieAndIdentificatie(current.getPlanidentificatie(), current.getIdentificatie());

            if (optionalFound.isPresent()) { // existing entry
                LettertekenaanduidingDto found = optionalFound.get();
                if (found.equals(current)) { // not changed
                    savedLettertekenaanduiding = found;
                    updateCounter.skipped();
                } else {                     // changed
                    found.setNaam(naam);
                    found.setLabelinfo(labelInfo);

                    found.setStyleid(styleid);
                    found.setMd5hash(md5hash);
                    savedLettertekenaanduiding = lettertekenaanduidingRepository.save(found);
                    updateCounter.updated();
                }
            } else { // new entry
                savedLettertekenaanduiding = lettertekenaanduidingRepository.save(current);
                updateCounter.add();
            }
        } catch (Exception e) {
            updateCounter.skipped();
            log.error("Error while processing: {} in lettertekenaanduiding processing: {}", lettertekenaanduiding, e);
        }
        return savedLettertekenaanduiding;
    }
}
