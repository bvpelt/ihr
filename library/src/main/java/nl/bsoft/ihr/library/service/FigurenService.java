package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Figuur;
import nl.bsoft.ihr.generated.model.FiguurCollectie;
import nl.bsoft.ihr.library.mapper.FiguurMapper;
import nl.bsoft.ihr.library.mapper.IllustratieMapper;
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
public class FigurenService {
    private final APIService APIService;
    private final ImroLoadRepository imroLoadRepository;
    private final FiguurRepository figuurRepository;
    private final ArtikelRepository artikelRepository;
    private final TekstRefRepository tekstRefRepository;
    private final IllustratieRepository illustratieRepository;
    private final LocatieRepository locatieRepository;
    private final FiguurMapper figuurMapper;
    private final IllustratieMapper illustratieMapper;
    private final LocatieMapper locatieMapper;
    private final int MAXFIGUREN = 100;

    @Autowired
    public FigurenService(APIService APIService,
                          ImroLoadRepository imroLoadRepository,
                          FiguurRepository figuurRepository,
                          ArtikelRepository artikelRepository,
                          TekstRefRepository tekstRefRepository,
                          IllustratieRepository illustratieRepository,
                          LocatieRepository locatieRepository,
                          FiguurMapper figuurMapper,
                          IllustratieMapper illustratieMapper,
                          LocatieMapper locatieMapper) {
        this.APIService = APIService;
        this.imroLoadRepository = imroLoadRepository;
        this.figuurRepository = figuurRepository;
        this.artikelRepository = artikelRepository;
        this.tekstRefRepository = tekstRefRepository;
        this.illustratieRepository = illustratieRepository;
        this.locatieRepository = locatieRepository;
        this.figuurMapper = figuurMapper;
        this.illustratieMapper = illustratieMapper;
        this.locatieMapper = locatieMapper;
    }

    public UpdateCounter loadFigurenFromList() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByIdentificatieNotLoaded();

        imroLoadDtos.forEach(
                imroPlan -> {
                    procesFiguren(imroPlan.getIdentificatie(), 1, updateCounter, imroPlan);
                    imroLoadRepository.save(imroPlan);
                }
        );
        return updateCounter;
    }

    public void procesFiguren(String planidentificatie, int page, UpdateCounter updateCounter, ImroLoadDto imroPlan) {
        FiguurCollectie figuren = getFigurenForId(planidentificatie, page);
        if (figuren != null) {
            saveFiguren(planidentificatie, page, figuren, updateCounter, imroPlan);
        }
    }

    private FiguurCollectie getFigurenForId(String planidentificatie, int page) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/figuren");
        uriComponentsBuilder.queryParam("pageSize", MAXFIGUREN);
        uriComponentsBuilder.queryParam("page", page);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), FiguurCollectie.class);
    }

    private void saveFiguren(String planidentificatie, int page, FiguurCollectie figuurCollectie, UpdateCounter updateCounter, ImroLoadDto imroPlan) {
        if (figuurCollectie != null) {
            if (figuurCollectie.getEmbedded() != null) {
                if (figuurCollectie.getEmbedded().getFiguren() != null) {
                    // add each found bouwaanduiding
                    figuurCollectie.getEmbedded().getFiguren().forEach(figuur -> {
                        addFiguur(planidentificatie, figuur, updateCounter);
                    });
                    // while maximum number of bouwaanduidingen retrieved, get next page
                    if (figuurCollectie.getEmbedded().getFiguren().size() == MAXFIGUREN) {
                        procesFiguren(planidentificatie, page + 1, updateCounter, imroPlan);
                    }
                    if (imroPlan != null) {
                        imroPlan.setFiguurloaded(true);
                    }
                }
            }
        }
        if (imroPlan != null) {
            imroPlan.setFiguurtried(true);
        }
    }

    @Transactional
    protected FiguurDto addFiguur(String planidentificatie, Figuur figuur, UpdateCounter updateCounter) {
        FiguurDto savedFiguur = null;

        try {
            FiguurDto current = figuurMapper.toMaatvoering(figuur);
            current.setPlanidentificatie(planidentificatie);
            String md5hash = null;

            if (figuur.getGeometrie() != null) {
                md5hash = DigestUtils.md5Hex(figuur.getGeometrie().toString().toUpperCase());
                current.setMd5hash(md5hash);

                Optional<LocatieDto> optionalLocatieDto = locatieRepository.findByMd5hash(md5hash);
                if (!optionalLocatieDto.isPresent()) {
                    LocatieDto locatieDto = locatieMapper.toLocatieDto(figuur);
                    locatieDto.setMd5hash(md5hash);
                    locatieDto.setRegistratie(LocalDateTime.now());
                    locatieRepository.save(locatieDto);
                    log.debug("Added locatie: {}", md5hash);
                }
            }

            Optional<FiguurDto> optionalFound = figuurRepository.findByPlanidentificatieAndIdentificatie(current.getPlanidentificatie(), current.getIdentificatie());

            if (optionalFound.isPresent()) { // existing entry
                FiguurDto found = optionalFound.get();
                if (found.equals(current)) { // not changed
                    savedFiguur = found;
                    updateCounter.skipped();
                } else {                     // changed
                    found.setNaam(current.getNaam());
                    found.setLabelinfo(current.getLabelinfo());
                    found.setStyleid(current.getStyleid());
                    found.setMd5hash(md5hash);
                    found.setMd5hash(md5hash);

                    current.getArtikelnummers().forEach(artikel -> {
                        if (!found.getArtikelnummers().contains(artikel)) {
                            found.addArtikel(artikel);
                        }
                    });

                    current.getVerwijzingnaartekst().forEach(tekst -> {
                        if (!found.getVerwijzingnaartekst().contains(tekst)) {
                            found.addVerwijzingNaarTekst(tekst);
                        }
                    });

                    current.getIllustraties().forEach(illustratie -> {
                        if (!found.getIllustraties().contains(illustratie)) {
                            found.addIllustratie(illustratie);
                        }
                    });

                    savedFiguur = figuurRepository.save(found);
                    updateCounter.updated();
                }
            } else { // new entry
                figuur.getArtikelnummers().forEach(artikel -> {
                    Optional<ArtikelDto> optionalArtikel = artikelRepository.findByArtikel(artikel);
                    ArtikelDto foundArtikel = null;
                    if (optionalArtikel.isPresent()) {
                        foundArtikel = optionalArtikel.get();
                        current.addArtikel(foundArtikel);
                    } else {
                        foundArtikel = new ArtikelDto();
                        foundArtikel.setArtikel(artikel);

                        ArtikelDto savedArtikel = artikelRepository.save(foundArtikel);
                        current.addArtikel(savedArtikel);
                    }
                });
                // tekstref
                figuur.getVerwijzingNaarTekst().forEach(tekst -> {
                    Optional<TekstRefDto> optionalTekstRef = tekstRefRepository.findByReferentie(tekst);
                    TekstRefDto foundTekstRef = null;
                    if (optionalTekstRef.isPresent()) {
                        foundTekstRef = optionalTekstRef.get();
                        current.addVerwijzingNaarTekst(foundTekstRef);
                    } else {
                        foundTekstRef = new TekstRefDto();
                        foundTekstRef.setReferentie(tekst);

                        TekstRefDto savedArtikel = tekstRefRepository.save(foundTekstRef);
                        current.addVerwijzingNaarTekst(savedArtikel);
                    }
                });
                // illustratie
                if (figuur.getIllustraties() != null) {
                    if (figuur.getIllustraties().get() != null) {
                        figuur.getIllustraties().get().forEach(illustratie -> {
                            try {
                                IllustratieDto usedIllustratie = illustratieMapper.toIllustratie(illustratie);
                                Optional<IllustratieDto> optionalIllustratie = illustratieRepository.findByHrefAndTypeAndNaamAndLegendanaam(usedIllustratie.getHref(), usedIllustratie.getType(), usedIllustratie.getNaam(), usedIllustratie.getLegendanaam());
                                IllustratieDto foundIllustratie = null;
                                if (optionalIllustratie.isPresent()) {
                                    foundIllustratie = optionalIllustratie.get();
                                    current.addIllustratie(foundIllustratie);
                                } else {
                                    foundIllustratie = new IllustratieDto();
                                    foundIllustratie.setHref(usedIllustratie.getHref());
                                    foundIllustratie.setType(usedIllustratie.getType());
                                    foundIllustratie.setNaam(usedIllustratie.getNaam());
                                    foundIllustratie.setLegendanaam(usedIllustratie.getLegendanaam());

                                    IllustratieDto savedIllustratie = illustratieRepository.save(foundIllustratie);
                                    current.addIllustratie(savedIllustratie);
                                }
                            } catch (Exception e) {
                                log.error("Error converting illustratie: {} for plan: {}, figuur: {}", illustratie, planidentificatie, figuur.getId());
                            }
                        });
                    }
                }
                savedFiguur = figuurRepository.save(current);
                updateCounter.add();
            }
        } catch (Exception e) {
            updateCounter.skipped();
            log.error("Error while processing: {} in figuur processing: {}", figuur, e);
        }
        return savedFiguur;
    }
}
