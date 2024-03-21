package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Bestemmingsvlak;
import nl.bsoft.ihr.generated.model.BestemmingsvlakCollectie;
import nl.bsoft.ihr.library.mapper.BestemmingsvlakMapper;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.model.dto.BestemmingsvlakDto;
import nl.bsoft.ihr.library.model.dto.ImroLoadDto;
import nl.bsoft.ihr.library.model.dto.LocatieDto;
import nl.bsoft.ihr.library.repository.*;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    private void saveBestemmingsvlakken(String planidentificatie, int page, BestemmingsvlakCollectie bestemmingsvlakken, UpdateCounter updateCounter) {
        if (bestemmingsvlakken != null) {

            if (bestemmingsvlakken.getEmbedded() != null) {
                if (bestemmingsvlakken.getEmbedded().getBestemmingsvlakken() != null) {
                    // add each found text
                    bestemmingsvlakken.getEmbedded().getBestemmingsvlakken().forEach(bestemmingsvlak -> {
                        addBestemmingsvlak(planidentificatie, bestemmingsvlak, updateCounter);
                    });
                    // while maximum number of teksten retrieved, get next page
                    if (bestemmingsvlakken.getEmbedded().getBestemmingsvlakken().size() == MAXBESTEMMINGSVLAKKEN) {
                        procesBestemmingsvlak(planidentificatie, page + 1, updateCounter);
                    }
                }
            }
        }
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

    private BestemmingsvlakDto addBestemmingsvlak(String planidentificatie, Bestemmingsvlak bestemmingsvlak, UpdateCounter updateCounter) {
        BestemmingsvlakDto savedBestemmingsvlak = null;

        try {
            final BestemmingsvlakDto current = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
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

            Optional<BestemmingsvlakDto> found = bestemmingsvlakRepository.findByPlanidentificatieAndIdentificatie(current.getPlanidentificatie(), current.getIdentificatie());

            if (found.isPresent()) {
                // if equal do not save
                if (found.get().equals(current)) {
                    updateCounter.skipped();
                    savedBestemmingsvlak = found.get();
                } else { // if changed update
                    BestemmingsvlakDto updated = found.get();
                    updated.setArtikelnummer(current.getArtikelnummer());
                    updated.setNaam(current.getNaam());
                    updated.setType(current.getType());
                    updated.setBestemmingshoofdgroep(current.getBestemmingshoofdgroep());
                    updated.setLabelInfo(current.getLabelInfo());
                    updated.setMd5hash(md5hash);

                    updated.getBestemmingsfuncties().forEach(bestemmingsfunctie -> {
                        // For each bestemmingsfunctie in database (updated)
                        //   not in to process message (current)
                        //   remove from updated
                        if (!current.getBestemmingsfuncties().contains(bestemmingsfunctie)) {
                            updated.getBestemmingsfuncties().remove(bestemmingsfunctie);
                        }
                    });
                    current.getBestemmingsfuncties().forEach(bestemmingsfunctie -> {
                        bestemmingFunctieRepository.save(bestemmingsfunctie);
                        //bestemmingsfunctie.getBestemmingsvlakken().add(updated);
                        updated.getBestemmingsfuncties().add(bestemmingsfunctie);

                    });

                    updated.getVerwijzingNaarTekst().forEach(verwijzing -> {
                        if (!current.getVerwijzingNaarTekst().contains(verwijzing)) {
                            updated.getVerwijzingNaarTekst().remove(verwijzing);
                        }
                    });
                    current.getVerwijzingNaarTekst().forEach(verwijzing -> {
                        tekstRefRepository.save(verwijzing);
                        //verwijzing.getBestemmingsvlakken().add(updated);
                        updated.getVerwijzingNaarTekst().add(verwijzing);

                    });

                    updateCounter.updated();

                    savedBestemmingsvlak = bestemmingsvlakRepository.save(updated);
                }
            } else { // new occurrence
                updateCounter.add();
                current.getBestemmingsfuncties().forEach(bestemmingsvlakfunctie -> {
                    bestemmingFunctieRepository.save(bestemmingsvlakfunctie);
                    //bestemmingsvlakfunctie.getBestemmingsvlakken().add(current);
                });
                current.getVerwijzingNaarTekst().forEach(verwijzingtekst -> {
                    tekstRefRepository.save(verwijzingtekst);
                    //verwijzingtekst.getBestemmingsvlakken().add(current);
                });
                savedBestemmingsvlak = bestemmingsvlakRepository.save(current);
            }
        } catch (Exception e) {
            log.error("Error while processing: {} in tekst processing: {}", bestemmingsvlak, e);
        }
        return savedBestemmingsvlak;
    }
}
