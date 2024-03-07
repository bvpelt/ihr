package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.*;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.mapper.PlanMapper;
import nl.bsoft.ihr.library.mapper.TekstMapper;
import nl.bsoft.ihr.library.model.dto.*;
import nl.bsoft.ihr.library.repository.*;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class TekstenService {
    //private final int MAX_PAGE_SIZE;
    private final APIService APIService;
    //private final PlanRepository planRepository;
    private final ImroLoadRepository imroLoadRepository;
    //private final LocatieRepository locatieRepository;
    //private final OverheidRepository overheidRepository;
    private final TekstRepository tekstRepository;
    //private final PlanMapper planMapper;
    //private final LocatieMapper locatieMapper;
    private final TekstMapper tekstMapper;

    private final int MAXTEKSTSIZE=100;

    @Autowired
    public TekstenService(APIService APIService,
                          ImroLoadRepository imroLoadRepository,
                          TekstRepository tekstRepository,
                          TekstMapper tekstMapper) {
        this.APIService = APIService;
        this.imroLoadRepository = imroLoadRepository;
        this.tekstRepository = tekstRepository;
        this.tekstMapper = tekstMapper;
    }

    public UpdateCounter loadTekstenFromList() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByIdentificatieNotLoaded();

        imroLoadDtos.forEach(
                imroPlan -> {
                    procesTekst(imroPlan.getIdentificatie(), 1, updateCounter);
                }
        );

        return updateCounter;
    }

    private void saveText(String identificatie, int page, TekstCollectie teksten, UpdateCounter updateCounter) {
        if (teksten != null) {
            if (teksten.getEmbedded() != null) {
                if (teksten.getEmbedded().getTeksten() != null) {
                    // add each found text
                    teksten.getEmbedded().getTeksten().forEach(tekst -> {
                        addTekst(identificatie, tekst, updateCounter);

                        List<TekstReferentie> tekstReferentieList = tekst.getLinks().getChildren();
                        tekstReferentieList.forEach(tekstReferentie -> {
                            String href = tekstReferentie.getHref();
                            procesTekstRef(identificatie, href, 1, updateCounter);
                        });
                    });

                    // while maximum number of teksten retrieved, get next page
                    if (teksten.getEmbedded().getTeksten().size() == MAXTEKSTSIZE) {
                        procesTekst(identificatie, page + 1, updateCounter);
                    }
                }
            }
        }
    }
    public void procesTekstRef(String identificatie, String href, int page, UpdateCounter updateCounter) {
        TekstCollectie teksten =  getTekstRef(href, page);

        if (teksten != null) {
            saveText(identificatie, page, teksten, updateCounter);
        }
    }

    public void procesTekst(String identificatie, int page, UpdateCounter updateCounter) {
        TekstCollectie teksten = getTekstenForId(identificatie, page);
        if (teksten != null ) {
            saveText(identificatie, page, teksten, updateCounter);
        }
    }

    private TekstCollectie getTekstRef(String ref, int page) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(ref);
        //uriComponentsBuilder.queryParam("pageSize", MAXTEKSTSIZE);
        //uriComponentsBuilder.queryParam("page", page);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), TekstCollectie.class);
    }

    private TekstCollectie getTekstenForId(String identificatie, int page) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + identificatie + "/teksten");
        uriComponentsBuilder.queryParam("pageSize", MAXTEKSTSIZE);
        uriComponentsBuilder.queryParam("page", page);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), TekstCollectie.class);
    }

    private TekstDto addTekst(String planidentificatie, Tekst tekst, UpdateCounter updateCounter) {
        TekstDto savedTekst = null;

        try {
            TekstDto current = tekstMapper.toTekst(tekst);
            current.setPlanidentificatie(planidentificatie);

            Optional<TekstDto> found = tekstRepository.findByPlanidentificatieAndTekstidentificatie(current.getPlanidentificatie(), current.getTekstidentificatie());

            if (found.isPresent()) {
                // if equal do not save
                if (found.equals(current)) {
                    updateCounter.skipped();
                } else { // if changed update
                    TekstDto updated = found.get();
                    updated.setTitel(current.getTitel());
                    updated.setInhoud(current.getInhoud());
                    updated.setExternLabel(current.getExternLabel());
                    updated.setVolgNummer(current.getVolgNummer());
                    updated.setExternHRef(current.getExternHRef());
                    updateCounter.updated();
                    current = updated;
                }
            } else { // new occurrence
                updateCounter.add();
            }
            savedTekst = tekstRepository.save(current);
        } catch (Exception e) {
            log.error("Error while processing: {} in tekst processing: {}", tekst, e);
        }
        return savedTekst;
    }
}
