package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Bestemmingsvlak;
import nl.bsoft.ihr.generated.model.BestemmingsvlakCollectie;
import nl.bsoft.ihr.library.mapper.BestemmingsvlakMapper;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.model.dto.*;
import nl.bsoft.ihr.library.repository.*;
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
public class BestemmingsvlakkenService {
    private final APIService APIService;
    private final ImroLoadRepository imroLoadRepository;
    private final BestemmingsvlakRepository bestemmingsvlakRepository;
    private final BestemmingFunctieRepository bestemmingFunctieRepository;
    private final TekstRefRepository tekstRefRepository;
    private final LocatieRepository locatieRepository;
    private final BestemmingsvlakMapper bestemmingsvlakMapper;
    private final LocatieMapper locatieMapper;
    private final int MAXBESTEMMINGSVLAKKEN = 100;

    @Autowired
    public BestemmingsvlakkenService(APIService APIService,
                                     ImroLoadRepository imroLoadRepository,
                                     BestemmingsvlakRepository bestemmingsvlakRepository,
                                     BestemmingFunctieRepository bestemmingFunctieRepository,
                                     TekstRefRepository tekstRefRepository,
                                     LocatieRepository locatieRepository,
                                     BestemmingsvlakMapper bestemmingsvlakMapper,
                                     LocatieMapper locatieMapper) {
        this.APIService = APIService;
        this.imroLoadRepository = imroLoadRepository;
        this.bestemmingsvlakRepository = bestemmingsvlakRepository;
        this.bestemmingFunctieRepository = bestemmingFunctieRepository;
        this.tekstRefRepository = tekstRefRepository;
        this.locatieRepository = locatieRepository;
        this.bestemmingsvlakMapper = bestemmingsvlakMapper;
        this.locatieMapper = locatieMapper;
    }

    public UpdateCounter loadTekstenFromList() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByIdentificatieNotLoaded();

        imroLoadDtos.forEach(
                imroPlan -> {
                    procesBestemmingsvlak(imroPlan.getIdentificatie(), 1, updateCounter);
                }
        );
        return updateCounter;
    }

    public void procesBestemmingsvlak(String planidentificatie, int page, UpdateCounter updateCounter) {
        BestemmingsvlakCollectie bestemmingsvlakCollectie = getBestemmingsvlakkenForId(planidentificatie, page);
        if (bestemmingsvlakCollectie != null) {
            saveBestemmingsvlakken(planidentificatie, page, bestemmingsvlakCollectie, updateCounter);
        }
    }

    private BestemmingsvlakCollectie getBestemmingsvlakkenForId(String planidentificatie, int page) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/bestemmingsvlakken");
        uriComponentsBuilder.queryParam("pageSize", MAXBESTEMMINGSVLAKKEN);
        uriComponentsBuilder.queryParam("page", page);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), BestemmingsvlakCollectie.class);
    }

    private void saveBestemmingsvlakken(String planidentificatie, int page, BestemmingsvlakCollectie bestemmingsvlakken, UpdateCounter updateCounter) {
        if (bestemmingsvlakken != null) {

            if (bestemmingsvlakken.getEmbedded() != null) {
                if (bestemmingsvlakken.getEmbedded().getBestemmingsvlakken() != null) {
                    // add each found bestemmingsvlak
                    bestemmingsvlakken.getEmbedded().getBestemmingsvlakken().forEach(bestemmingsvlak -> {
                        addBestemmingsvlak(planidentificatie, bestemmingsvlak, updateCounter);
                    });
                    // while maximum number of bestemmingsvlakken retrieved, get next page
                    if (bestemmingsvlakken.getEmbedded().getBestemmingsvlakken().size() == MAXBESTEMMINGSVLAKKEN) {
                        procesBestemmingsvlak(planidentificatie, page + 1, updateCounter);
                    }
                }
            }
        }
    }

    @Transactional
    protected BestemmingsvlakDto addBestemmingsvlak(String planidentificatie, Bestemmingsvlak bestemmingsvlak, UpdateCounter updateCounter) {
        BestemmingsvlakDto savedBestemmingsvlak = null;

        try {
            BestemmingsvlakDto current = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            current.setPlanidentificatie(planidentificatie);
            String md5hash = null;

            if (bestemmingsvlak.getGeometrie() != null) {
                md5hash = DigestUtils.md5Hex(bestemmingsvlak.getGeometrie().toString().toUpperCase());
                current.setMd5hash(md5hash);

                Optional<LocatieDto> optionalLocatieDto = locatieRepository.findByMd5hash(md5hash);
                if (!optionalLocatieDto.isPresent()) {
                    LocatieDto locatieDto = locatieMapper.toLocatieDto(bestemmingsvlak);
                    locatieDto.setMd5hash(md5hash);
                    locatieDto.setRegistratie(LocalDateTime.now());
                    locatieRepository.save(locatieDto);
                    log.debug("Added locatie: {}", md5hash);
                }
            }
            Optional<BestemmingsvlakDto> optionalFound = bestemmingsvlakRepository.findByPlanidentificatieAndIdentificatie(current.getPlanidentificatie(), current.getIdentificatie());

            if (optionalFound.isPresent()) { // existing entry
                BestemmingsvlakDto found = optionalFound.get();
                if (found.equals(current)) { // not changed
                    savedBestemmingsvlak = found;
                    updateCounter.skipped();
                } else {                     // changed
                    found.setType(current.getType());
                    found.setNaam(current.getNaam());

                    found.setBestemmingshoofdgroep(current.getBestemmingshoofdgroep());
                    found.setArtikelnummer(current.getArtikelnummer());
                    found.setLabelInfo(current.getLabelInfo());
                    found.setMd5hash(md5hash);

                    current.getBestemmingsfuncties().forEach(bestemmingFunctie -> {
                        if (!found.getBestemmingsfuncties().contains(bestemmingFunctie)) {
                            found.addBestemmingsfunctie(bestemmingFunctie);
                        }
                    });

                    current.getVerwijzingNaarTekst().forEach(tekstRef -> {
                        if (!found.getVerwijzingNaarTekst().contains(tekstRef)) {
                            found.addVerwijzingNaarTekst(tekstRef);
                        }
                    });
                    savedBestemmingsvlak = bestemmingsvlakRepository.save(found);
                    updateCounter.updated();
                }
            } else { // new entry
                current.getBestemmingsfuncties().forEach(bestemmingFunctie -> {
                    Optional<BestemmingFunctieDto> optionalBestemmingsFunctie = bestemmingFunctieRepository.findByBestemmingsfunctieAndFunctieniveau(bestemmingFunctie.getBestemmingsfunctie(), bestemmingFunctie.getFunctieniveau());
                    if (optionalBestemmingsFunctie.isPresent()) { // exist
                        current.removeBestemmingsfunctie(bestemmingFunctie);
                        BestemmingFunctieDto currentBestemmingFunctie = optionalBestemmingsFunctie.get();
                        current.addBestemmingsfunctie(currentBestemmingFunctie);
                    } else {
                        BestemmingFunctieDto savedBestemmingsfunctie = bestemmingFunctieRepository.save(bestemmingFunctie);
                        current.addBestemmingsfunctie(savedBestemmingsfunctie);
                    }
                });

                current.getVerwijzingNaarTekst().forEach(tekstRef -> {
                    Optional<TekstRefDto> optionalTekstRef = tekstRefRepository.findByReferentie(tekstRef.getReferentie());
                    if (optionalTekstRef.isPresent()) {
                        current.removeVerwijzingNaarTekst(tekstRef);
                        TekstRefDto currentTekstRef = optionalTekstRef.get();
                        current.addVerwijzingNaarTekst(currentTekstRef);
                    } else {
                        TekstRefDto savedTekstRef = tekstRefRepository.save(tekstRef);
                        current.addVerwijzingNaarTekst(savedTekstRef);
                    }
                });

                savedBestemmingsvlak = bestemmingsvlakRepository.save(current);
                updateCounter.add();
            }
        } catch (Exception e) {
            updateCounter.skipped();
            log.error("Error while processing: {} in bestemmingsvlak processing: {}", bestemmingsvlak, e);
        }
        return savedBestemmingsvlak;
    }
}
