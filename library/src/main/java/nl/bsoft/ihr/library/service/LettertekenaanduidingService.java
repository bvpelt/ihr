package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Lettertekenaanduiding;
import nl.bsoft.ihr.generated.model.LettertekenaanduidingCollectie;
import nl.bsoft.ihr.library.mapper.LettertekenaanduidingMapper;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.model.dto.BestemmingFunctieDto;
import nl.bsoft.ihr.library.model.dto.ImroLoadDto;
import nl.bsoft.ihr.library.model.dto.LettertekenaanduidingDto;
import nl.bsoft.ihr.library.model.dto.LocatieDto;
import nl.bsoft.ihr.library.repository.BestemmingFunctieRepository;
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
import java.util.Optional;

@Slf4j
@Service
public class LettertekenaanduidingService {
    private final APIService APIService;
    private final ImroLoadRepository imroLoadRepository;
    private final LettertekenaanduidingRepository lettertekenaanduidingRepository;
    private final BestemmingFunctieRepository bestemmingFunctieRepository;
    private final LocatieRepository locatieRepository;
    private final LettertekenaanduidingMapper lettertekenaanduidingMapper;
    private final LocatieMapper locatieMapper;
    private final int MAXLETTERTEKENAANDUIDINGEN = 100;

    @Autowired
    public LettertekenaanduidingService(APIService APIService,
                                        ImroLoadRepository imroLoadRepository,
                                        BestemmingFunctieRepository bestemmingFunctieRepository,
                                        LettertekenaanduidingRepository lettertekenaanduidingRepository,
                                        LocatieRepository locatieRepository,
                                        LettertekenaanduidingMapper lettertekenaanduidingMapper,
                                        LocatieMapper locatieMapper) {
        this.APIService = APIService;
        this.imroLoadRepository = imroLoadRepository;
        this.bestemmingFunctieRepository = bestemmingFunctieRepository;
        this.lettertekenaanduidingRepository = lettertekenaanduidingRepository;
        this.locatieRepository = locatieRepository;
        this.lettertekenaanduidingMapper = lettertekenaanduidingMapper;
        this.locatieMapper = locatieMapper;
    }

    public UpdateCounter loadLettertekenaanduidingenFromList() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByLettertekenaanduidingNotTried();

        imroLoadDtos.forEach(
                imroPlan -> {
                    procesLettertekenaanduiding(imroPlan.getIdentificatie(), 1, updateCounter, imroPlan);
                }
        );
        return updateCounter;
    }

    public void procesLettertekenaanduiding(String planidentificatie, int page, UpdateCounter updateCounter, ImroLoadDto imroPlan) {
        LettertekenaanduidingCollectie lettertekenaanduidingen = getLettertekenaanduidingForId(planidentificatie, page);
        if (lettertekenaanduidingen != null) {
            saveLettertekenaanduidingen(planidentificatie, page, lettertekenaanduidingen, updateCounter, imroPlan);
        }
    }

    private LettertekenaanduidingCollectie getLettertekenaanduidingForId(String planidentificatie, int page) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/lettertekenaanduidingen");
        uriComponentsBuilder.queryParam("pageSize", MAXLETTERTEKENAANDUIDINGEN);
        uriComponentsBuilder.queryParam("page", page);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), LettertekenaanduidingCollectie.class);
    }

    private void saveLettertekenaanduidingen(String planidentificatie, int page, LettertekenaanduidingCollectie lettertekenaanduidingCollectie, UpdateCounter updateCounter, ImroLoadDto imroPlan) {
        if (lettertekenaanduidingCollectie != null) {
            if (lettertekenaanduidingCollectie.getEmbedded() != null) {
                if (lettertekenaanduidingCollectie.getEmbedded().getLettertekenaanduidingen() != null) {
                    // add each found bouwaanduiding
                    lettertekenaanduidingCollectie.getEmbedded().getLettertekenaanduidingen().forEach(letteraanduiding -> {
                        addLettertekenaanduiding(planidentificatie, letteraanduiding, updateCounter);
                    });
                    // while maximum number of bouwaanduidingen retrieved, get next page
                    if (lettertekenaanduidingCollectie.getEmbedded().getLettertekenaanduidingen().size() == MAXLETTERTEKENAANDUIDINGEN) {
                        procesLettertekenaanduiding(planidentificatie, page + 1, updateCounter, imroPlan);
                    }
                    imroPlan.setLettertekenaanduidingloaded(true);
                }
            }
        }
        imroPlan.setLettertekenaanduidingtried(true);
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

            Optional<LettertekenaanduidingDto> optionalFound = lettertekenaanduidingRepository.findByPlanidentificatieAndIdentificatie(current.getPlanidentificatie(), current.getIdentificatie());

            if (optionalFound.isPresent()) { // existing entry
                LettertekenaanduidingDto found = optionalFound.get();
                if (found.equals(current)) { // not changed
                    savedLettertekenaanduiding = found;
                    updateCounter.skipped();
                } else {                     // changed
                    found.setNaam(current.getNaam());
                    found.setLabelinfo(current.getLabelinfo());
                    found.setStyleid(current.getStyleid());
                    found.setMd5hash(md5hash);

                    current.getBestemmingfuncties().forEach(bestemmingsfunctie -> {
                        if (!found.getBestemmingfuncties().contains(bestemmingsfunctie)) {
                            found.addBestemmingsfunctie(bestemmingsfunctie);
                        }
                    });
                    savedLettertekenaanduiding = lettertekenaanduidingRepository.save(found);
                    updateCounter.updated();
                }
            } else { // new entry
                lettertekenaanduiding.getBestemmingsfuncties().forEach(bestemmingsfunctie -> {
                    String functie = bestemmingsfunctie.getBestemmingsfunctie();
                    String functieniveau = bestemmingsfunctie.getFunctieniveau();

                    Optional<BestemmingFunctieDto> optionalBestemmingFunctie = bestemmingFunctieRepository.findByBestemmingsfunctieAndFunctieniveau(functie, functieniveau);
                    BestemmingFunctieDto foundBestemmingsFunctie = null;
                    if (optionalBestemmingFunctie.isPresent()) {
                        foundBestemmingsFunctie = optionalBestemmingFunctie.get();
                        current.addBestemmingsfunctie(foundBestemmingsFunctie);
                    } else {
                        foundBestemmingsFunctie = new BestemmingFunctieDto();
                        foundBestemmingsFunctie.setBestemmingsfunctie(bestemmingsfunctie.getBestemmingsfunctie());
                        foundBestemmingsFunctie.setFunctieniveau(bestemmingsfunctie.getFunctieniveau());
                        BestemmingFunctieDto savedBestemmingsfunctie = bestemmingFunctieRepository.save(foundBestemmingsFunctie);
                        current.addBestemmingsfunctie(savedBestemmingsfunctie);
                    }
                });

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
