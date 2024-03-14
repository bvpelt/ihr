package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Structuurvisiegebied;
import nl.bsoft.ihr.generated.model.StructuurvisiegebiedCollectie;
import nl.bsoft.ihr.library.mapper.StructuurVisieGebiedMapper;
import nl.bsoft.ihr.library.model.dto.*;
import nl.bsoft.ihr.library.repository.*;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class StructuurVisieGebiedService {
    private final APIService APIService;
    private final ImroLoadRepository imroLoadRepository;
    private final StructuurvisieGebiedRepository structuurvisieGebiedRepository;
    private final StructuurvisieGebiedThemaRepository structuurvisieGebiedThemaRepository;
    private final StructuurvisieGebiedBeleidRepository structuurvisieGebiedBeleidRepository;
    private final TekstRefRepository tekstRefRepository;
    private final StructuurVisieGebiedMapper structuurVisieGebiedMapper;

    private final int MAXBESTEMMINGSVLAKKEN = 100;

    @Autowired
    public StructuurVisieGebiedService(APIService APIService,
                                       ImroLoadRepository imroLoadRepository,
                                       StructuurvisieGebiedRepository structuurvisieGebiedRepository,
                                       StructuurvisieGebiedThemaRepository structuurvisieGebiedThemaRepository,
                                       StructuurvisieGebiedBeleidRepository structuurvisieGebiedBeleidRepository,
                                       TekstRefRepository tekstRefRepository,
                                       StructuurVisieGebiedMapper structuurVisieGebiedMapper) {
        this.APIService = APIService;
        this.imroLoadRepository = imroLoadRepository;
        this.structuurvisieGebiedRepository = structuurvisieGebiedRepository;
        this.structuurvisieGebiedBeleidRepository = structuurvisieGebiedBeleidRepository;
        this.structuurvisieGebiedThemaRepository = structuurvisieGebiedThemaRepository;
        this.tekstRefRepository = tekstRefRepository;
        this.structuurVisieGebiedMapper = structuurVisieGebiedMapper;
    }

    public UpdateCounter loadTekstenFromList() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByIdentificatieNotLoaded();

        imroLoadDtos.forEach(
                imroPlan -> {
                    procesStructuurVisieGebied(imroPlan.getIdentificatie(), 1, updateCounter);
                }
        );
        return updateCounter;
    }

    public void procesStructuurVisieGebied(String planidentificatie, int page, UpdateCounter updateCounter) {
        StructuurvisiegebiedCollectie structuurvisiegebiedCollectie = getStructuurvisiegebiedForId(planidentificatie, page);
        if (structuurvisiegebiedCollectie != null) {
            saveStructuurvisiegebieden(planidentificatie, page, structuurvisiegebiedCollectie, updateCounter);
        }
    }

    private void saveStructuurvisiegebieden(String planidentificatie, int page, StructuurvisiegebiedCollectie structuurvisies, UpdateCounter updateCounter) {
        if (structuurvisies != null) {
            if (structuurvisies.getEmbedded() != null) {
                if (structuurvisies.getEmbedded().getStructuurvisiegebieden() != null) {
                    structuurvisies.getEmbedded().getStructuurvisiegebieden().forEach(structuurvisie -> {
                        addStructuurVisie(planidentificatie, structuurvisie, updateCounter);
                    });

                    if (structuurvisies.getEmbedded().getStructuurvisiegebieden().size() == MAXBESTEMMINGSVLAKKEN) {
                        procesStructuurVisieGebied(planidentificatie, page + 1, updateCounter);
                    }
                }
            }
        }
    }

    private StructuurvisiegebiedCollectie getStructuurvisiegebiedForId(String planidentificatie, int page) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/structuurvisiegebieden");
        uriComponentsBuilder.queryParam("pageSize", MAXBESTEMMINGSVLAKKEN);
        uriComponentsBuilder.queryParam("page", page);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), StructuurvisiegebiedCollectie.class);
    }

    private StructuurVisieGebiedDto addStructuurVisie(String planidentificatie, Structuurvisiegebied structuurvisie, UpdateCounter updateCounter) {
        StructuurVisieGebiedDto savedStructuurVisieGebiedDto = null;
        try {
            StructuurVisieGebiedDto current = structuurVisieGebiedMapper.toStructuurVisieGebied(structuurvisie);
            current.setPlanidentificatie(planidentificatie);

            Optional<StructuurVisieGebiedDto> found = structuurvisieGebiedRepository.findByPlanidentificatieAndIdentificatie(current.getPlanidentificatie(), current.getIdentificatie());

            if (found.isPresent()) {
                if (found.get().equals(current)) {
                    updateCounter.skipped();
                    savedStructuurVisieGebiedDto = found.get();
                } else {
                    StructuurVisieGebiedDto updated = found.get();
                    updated.setNaam(current.getNaam());
                    updated.setBeleid(current.getBeleid());
                    updated.setThema(current.getThema());
                    updated.setVerwijzingNaarTekst(current.getVerwijzingNaarTekst());
                    updateCounter.updated();
                    current = updated;
                    savedStructuurVisieGebiedDto = structuurvisieGebiedRepository.save(current);
                }
            } else {
                updateCounter.add();
                Set<StructuurVisieGebiedThemaDto> themaDtoSet = current.getThema();
                Set<StructuurVisieGebiedBeleidDto> beleidDtoSet = current.getBeleid();
                Set<TekstRefDto> tekstRefDtoSet = current.getVerwijzingNaarTekst();

                if ((themaDtoSet != null) && (themaDtoSet.size() > 0)) {
                    themaDtoSet.forEach(thema -> {
                        structuurvisieGebiedThemaRepository.save(thema);
                    });
                }

                if ((beleidDtoSet != null) && (beleidDtoSet.size() > 0)) {
                    beleidDtoSet.forEach(beleid -> {
                        structuurvisieGebiedBeleidRepository.save(beleid);
                    });
                }

                if ((tekstRefDtoSet != null) && (tekstRefDtoSet.size() > 0)) {
                    tekstRefDtoSet.forEach(tekstref -> {
                        tekstRefRepository.save(tekstref);
                    });
                }

                savedStructuurVisieGebiedDto = structuurvisieGebiedRepository.save(current);
            }
        } catch (Exception e) {
            log.error("Error while processing: {} in processing: {}", structuurvisie, e);
        }
        return savedStructuurVisieGebiedDto;
    }

}
