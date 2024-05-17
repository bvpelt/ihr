package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Kruimel;
import nl.bsoft.ihr.generated.model.Tekst;
import nl.bsoft.ihr.generated.model.TekstCollectie;
import nl.bsoft.ihr.library.mapper.TekstMapper;
import nl.bsoft.ihr.library.model.dto.AuditLogDto;
import nl.bsoft.ihr.library.model.dto.ImroLoadDto;
import nl.bsoft.ihr.library.model.dto.KruimelDto;
import nl.bsoft.ihr.library.model.dto.TekstDto;
import nl.bsoft.ihr.library.repository.AuditLogRepository;
import nl.bsoft.ihr.library.repository.ImroLoadRepository;
import nl.bsoft.ihr.library.repository.KruimelRepository;
import nl.bsoft.ihr.library.repository.TekstRepository;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TekstenService {
    private final APIService APIService;
    private final ImroLoadRepository imroLoadRepository;
    private final TekstRepository tekstRepository;
    private final KruimelRepository kruimelRepository;
    private final AuditLogRepository auditLogRepository;
    private final TekstMapper tekstMapper;
    private final int MAXTEKSTSIZE = 100;

    @Autowired
    public TekstenService(APIService APIService,
                          ImroLoadRepository imroLoadRepository,
                          TekstRepository tekstRepository,
                          KruimelRepository kruimelRepository,
                          AuditLogRepository auditLogRepository,
                          TekstMapper tekstMapper) {
        this.APIService = APIService;
        this.imroLoadRepository = imroLoadRepository;
        this.tekstRepository = tekstRepository;
        this.kruimelRepository = kruimelRepository;
        this.auditLogRepository = auditLogRepository;
        this.tekstMapper = tekstMapper;
    }

    public UpdateCounter loadTekstenFromList() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByTekstenNotTried();

        imroLoadDtos.forEach(
                imroPlan -> {
                    procesTeksten(imroPlan.getIdentificatie(), 1, updateCounter, imroPlan);
                    imroLoadRepository.save(imroPlan);
                }
        );
        return updateCounter;
    }


    public void procesTeksten(String planidentificatie, int page, UpdateCounter updateCounter, ImroLoadDto imroPlan) {
        TekstCollectie teksten = getTekstenForId(planidentificatie, page);
        if (teksten != null) {
            saveText(planidentificatie, page, teksten, updateCounter, imroPlan);
            if (imroPlan != null) {
                imroPlan.setTekstenLoaded(true);
                imroPlan.setTekstenloadedprocesed(updateCounter.getProcessed());
            }
        }
        if (imroPlan != null) {
            imroPlan.setTekstentried(true);
        }
    }

    private TekstCollectie getTekstenForId(String planidentificatie, int page) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/teksten");
        uriComponentsBuilder.queryParam("pageSize", MAXTEKSTSIZE);
        uriComponentsBuilder.queryParam("page", page);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), TekstCollectie.class);
    }

    private void saveText(String identificatie, int page, TekstCollectie teksten, UpdateCounter updateCounter, ImroLoadDto imroPlan) {
        if (teksten != null) {
            if (teksten.getEmbedded() != null) {
                if (teksten.getEmbedded().getTeksten() != null) {
                    // add each found text
                    teksten.getEmbedded().getTeksten().forEach(tekst -> {
                        addTekst(identificatie, tekst, updateCounter);
                    });

                    // while maximum number of teksten retrieved, get next page
                    if (teksten.getEmbedded().getTeksten().size() == MAXTEKSTSIZE) {
                        procesTeksten(identificatie, page + 1, updateCounter, imroPlan);
                    }
                }
            }
        }
    }

    @Transactional
    protected TekstDto addTekst(String planidentificatie, Tekst tekst, UpdateCounter updateCounter) {
        TekstDto savedTekst = null;

        try {
            String actie = "";
            TekstDto current = tekstMapper.toTekst(tekst);
            current.setPlanidentificatie(planidentificatie);

            Optional<TekstDto> found = tekstRepository.findByPlanidentificatieAndTekstidentificatie(current.getPlanidentificatie(), current.getTekstidentificatie());

            if (found.isPresent()) {
                // if equal do not save
                if (found.get().equals(current)) {
                    actie = "skipped";
                    updateCounter.skipped();
                    savedTekst = found.get();
                } else { // if changed update
                    actie = "updated";
                    TekstDto updated = found.get();
                    updated.setTitel(current.getTitel());
                    updated.setInhoud(current.getInhoud());
                    updated.setExternLabel(current.getExternLabel());
                    updated.setVolgNummer(current.getVolgNummer());
                    updated.setExternHRef(current.getExternHRef());
                    updateCounter.updated();
                    current = updated;
                    savedTekst = tekstRepository.save(current);
                }
            } else { // new occurrence
                actie = "add";
                updateCounter.add();
                savedTekst = tekstRepository.save(current);
            }

            List<Kruimel> kruimels = tekst.getKruimelpad();
            Iterator<Kruimel> kruimelIterator = kruimels.iterator();
            while (kruimelIterator.hasNext()) {
                Kruimel kruimel = kruimelIterator.next();

                String identificatie = kruimel.getId();
                String titel = kruimel.getTitel().isPresent() ? kruimel.getTitel().get() : null;
                Integer volgnummer = kruimel.getVolgnummer();
                Optional<KruimelDto> optionalKruimelDto = kruimelRepository.findByIdentificatieAndTitelAndVolgnummer(identificatie, titel, volgnummer);

                KruimelDto currentKruimel = null;
                if (optionalKruimelDto.isPresent()) {
                    currentKruimel = optionalKruimelDto.get();
                } else {
                    currentKruimel = new KruimelDto();
                    currentKruimel.setIdentificatie(identificatie);
                    currentKruimel.setTitel(titel);
                    currentKruimel.setVolgnummer(volgnummer);
                }
                currentKruimel.setKruimelpad(savedTekst);
                kruimelRepository.save(currentKruimel);
            }
            AuditLogDto auditLogDto = new AuditLogDto(planidentificatie, savedTekst.getTekstidentificatie(), "tekst", actie);
            auditLogRepository.save(auditLogDto);
        } catch (Exception e) {
            log.error("Error while processing: {} in tekst processing: {}", tekst, e);
        }
        return savedTekst;
    }

}
