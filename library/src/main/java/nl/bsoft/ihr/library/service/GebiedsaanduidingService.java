package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Bestemmingsvlak;
import nl.bsoft.ihr.generated.model.Gebiedsaanduiding;
import nl.bsoft.ihr.library.mapper.BestemmingsvlakMapper;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.repository.GebeidsaanduidingRepository;
import nl.bsoft.ihr.library.repository.ImroLoadRepository;
import nl.bsoft.ihr.library.repository.LocatieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class GebiedsaanduidingService {
    private final APIService APIService;
    private final ImroLoadRepository imroLoadRepository;
    private final GebeidsaanduidingRepository gebeidsaanduidingRepository;
    private final LocatieRepository locatieRepository;
    private final BestemmingsvlakMapper bestemmingsvlakMapper;
    private final LocatieMapper locatieMapper;
    private final int MAXBESTEMMINGSVLAKKEN = 100;

    @Autowired
    public GebiedsaanduidingService(APIService APIService,
                                    ImroLoadRepository imroLoadRepository,
                                    GebeidsaanduidingRepository gebeidsaanduidingRepository,
                                    LocatieRepository locatieRepository,
                                    BestemmingsvlakMapper bestemmingsvlakMapper,
                                    LocatieMapper locatieMapper) {
        this.APIService = APIService;
        this.imroLoadRepository = imroLoadRepository;
        this.gebeidsaanduidingRepository = gebeidsaanduidingRepository;
        this.locatieRepository = locatieRepository;
        this.bestemmingsvlakMapper = bestemmingsvlakMapper;
        this.locatieMapper = locatieMapper;
    }
    /*
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
            BestemmingsvlakDto current = bestemmingsvlakMapper.toBestemmingsvlak(bestemmingsvlak);
            current.setPlanidentificatie(planidentificatie);

            String md5hash = null;

            if (bestemmingsvlak.getGeometrie() != null ) {
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

                    updateCounter.updated();
                    current = updated;
                    savedBestemmingsvlak = bestemmingsvlakRepository.save(current);
                }
            } else { // new occurrence
                updateCounter.add();
                savedBestemmingsvlak = bestemmingsvlakRepository.save(current);
            }
        } catch (Exception e) {
            log.error("Error while processing: {} in tekst processing: {}", bestemmingsvlak, e);
        }
        return savedBestemmingsvlak;
    }

     */
}
