package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Structuurvisiegebied;
import nl.bsoft.ihr.generated.model.StructuurvisiegebiedCollectie;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.mapper.StructuurVisieGebiedMapper;
import nl.bsoft.ihr.library.model.dto.*;
import nl.bsoft.ihr.library.repository.*;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class StructuurVisieGebiedService {
    private final APIService APIService;
    private final ImroLoadRepository imroLoadRepository;
    private final StructuurvisieGebiedRepository structuurvisieGebiedRepository;
    private final ThemaRepository structuurvisieGebiedThemaRepository;
    private final StructuurvisieGebiedBeleidRepository structuurvisieGebiedBeleidRepository;
    private final TekstRefRepository tekstRefRepository;
    private final LocatieRepository locatieRepository;
    private final StructuurVisieGebiedMapper structuurVisieGebiedMapper;
    private final LocatieMapper locatieMapper;

    private final int MAXBESTEMMINGSVLAKKEN = 100;

    @Autowired
    public StructuurVisieGebiedService(APIService APIService,
                                       ImroLoadRepository imroLoadRepository,
                                       StructuurvisieGebiedRepository structuurvisieGebiedRepository,
                                       ThemaRepository structuurvisieGebiedThemaRepository,
                                       StructuurvisieGebiedBeleidRepository structuurvisieGebiedBeleidRepository,
                                       TekstRefRepository tekstRefRepository,
                                       LocatieRepository locatieRepository,
                                       StructuurVisieGebiedMapper structuurVisieGebiedMapper,
                                       LocatieMapper locatieMapper) {
        this.APIService = APIService;
        this.imroLoadRepository = imroLoadRepository;
        this.structuurvisieGebiedRepository = structuurvisieGebiedRepository;
        this.structuurvisieGebiedBeleidRepository = structuurvisieGebiedBeleidRepository;
        this.structuurvisieGebiedThemaRepository = structuurvisieGebiedThemaRepository;
        this.tekstRefRepository = tekstRefRepository;
        this.locatieRepository = locatieRepository;
        this.structuurVisieGebiedMapper = structuurVisieGebiedMapper;
        this.locatieMapper = locatieMapper;
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

            if (structuurvisie.getGeometrie() != null) {
                String md5hash = DigestUtils.md5Hex(structuurvisie.getGeometrie().toString().toUpperCase());
                current.setMd5hash(md5hash);

                Optional<LocatieDto> optionalLocatieDto = locatieRepository.findByMd5hash(md5hash);
                if (!optionalLocatieDto.isPresent()) {
                    LocatieDto locatieDto = locatieMapper.toLocatieDto(structuurvisie);
                    locatieDto.setMd5hash(md5hash);
                    locatieDto.setRegistratie(LocalDateTime.now());
                    locatieRepository.save(locatieDto);
                    log.debug("Added locatie: {}", md5hash);
                }
            }

            Optional<StructuurVisieGebiedDto> optionalFound = structuurvisieGebiedRepository.findByPlanidentificatieAndIdentificatie(current.getPlanidentificatie(), current.getIdentificatie());

            if (optionalFound.isPresent()) { // existing entry
                StructuurVisieGebiedDto found = optionalFound.get();
                if (found.equals(current)) { // not changed
                    savedStructuurVisieGebiedDto = found;
                    updateCounter.skipped();
                } else {                     // changed
                    found.setNaam(current.getNaam());
                    found.setMd5hash(current.getMd5hash());

                    StructuurVisieGebiedDto updated = optionalFound.get();
                    updated.setNaam(current.getNaam());

                    updated.setVerwijzingNaarTekst(current.getVerwijzingNaarTekst());
                    updateCounter.updated();

                    savedStructuurVisieGebiedDto = structuurvisieGebiedRepository.save(updated);

                    Set<BeleidDto>  structuurVisieGebiedBeleidDtoSet = saveStructuurVisieBeleid(savedStructuurVisieGebiedDto, current.getBeleid());
                    updated.setBeleid(structuurVisieGebiedBeleidDtoSet);
                    Set<ThemaDto> structuurVisieGebiedThemaDtoSet = saveStructuurVisieThema(savedStructuurVisieGebiedDto, current.getThema());
                    updated.setThema(structuurVisieGebiedThemaDtoSet);

                    savedStructuurVisieGebiedDto = structuurvisieGebiedRepository.save(updated);
                }
            } else {
                updateCounter.add();
                Set<BeleidDto> newBeleid = current.getBeleid();
                Set<ThemaDto> newThema = current.getThema();
                Set<TekstRefDto> newTeksref = current.getVerwijzingNaarTekst();

                current.setBeleid(null);
                current.setThema(null);
                current.setVerwijzingNaarTekst(null);
                savedStructuurVisieGebiedDto = structuurvisieGebiedRepository.save(current);

                Set<BeleidDto>  structuurVisieGebiedBeleidDtoSet = saveStructuurVisieBeleid(savedStructuurVisieGebiedDto, newBeleid);
                savedStructuurVisieGebiedDto.setBeleid(structuurVisieGebiedBeleidDtoSet);

                Set<ThemaDto> structuurVisieGebiedThemaDtoSet = saveStructuurVisieThema(savedStructuurVisieGebiedDto, newThema);
                savedStructuurVisieGebiedDto.setThema(structuurVisieGebiedThemaDtoSet);


                Set<TekstRefDto>  tekstRefDtos = saveTekstRefs(savedStructuurVisieGebiedDto,newTeksref );
                savedStructuurVisieGebiedDto.setVerwijzingNaarTekst(tekstRefDtos);

                savedStructuurVisieGebiedDto = structuurvisieGebiedRepository.save(savedStructuurVisieGebiedDto);

            }
        } catch (Exception e) {
            log.error("Error while processing: {} in processing: {}", structuurvisie, e);
        }
        return savedStructuurVisieGebiedDto;
    }

    private Set<TekstRefDto> saveTekstRefs(StructuurVisieGebiedDto savedStructuurVisieGebiedDto, Set<TekstRefDto>  teksref) {
        Set<TekstRefDto>  savedTekstref = new HashSet<>();

        Iterator<TekstRefDto> tekstRefDtoIterator = teksref.iterator();

        while (tekstRefDtoIterator.hasNext()) {
            TekstRefDto current = tekstRefDtoIterator.next();

            Optional<TekstRefDto> found = tekstRefRepository.findByReferentie(current.getReferentie());
            TekstRefDto currentTekstRef = null;
            if (found.isPresent()) {
                currentTekstRef = found.get();
                currentTekstRef.setStructuurvisiegebied(savedStructuurVisieGebiedDto);
                currentTekstRef = tekstRefRepository.save(currentTekstRef);
            } else {
                current.setStructuurvisiegebied(savedStructuurVisieGebiedDto);
                currentTekstRef = tekstRefRepository.save(current);
            }
            savedTekstref.add(currentTekstRef);
        }
        return savedTekstref;
    }

    private Set<ThemaDto> saveStructuurVisieThema(StructuurVisieGebiedDto savedStructuurVisieGebiedDto, Set<ThemaDto> thema) {
        Set<ThemaDto> savedThema = new HashSet<>();

        Iterator<ThemaDto> structuurVisieGebiedThemaDtoIterator = thema.iterator();

        while (structuurVisieGebiedThemaDtoIterator.hasNext()) {
            ThemaDto current = structuurVisieGebiedThemaDtoIterator.next();

            Optional<ThemaDto> found = structuurvisieGebiedThemaRepository.findByThema(current.getThema());
            ThemaDto currentStructuurVisieGebiedThemaDto = null;
            if (found.isPresent()) {
                currentStructuurVisieGebiedThemaDto = found.get();
                currentStructuurVisieGebiedThemaDto.setStructuurVisieGebied(savedStructuurVisieGebiedDto);
                currentStructuurVisieGebiedThemaDto = structuurvisieGebiedThemaRepository.save(currentStructuurVisieGebiedThemaDto);
            } else {
                current.setStructuurVisieGebied(savedStructuurVisieGebiedDto);
                currentStructuurVisieGebiedThemaDto = structuurvisieGebiedThemaRepository.save(current);
            }
            savedThema.add(currentStructuurVisieGebiedThemaDto);
        }
        return savedThema;
    }

    private Set<BeleidDto>  saveStructuurVisieBeleid(StructuurVisieGebiedDto savedStructuurVisieGebiedDto, Set<BeleidDto> beleid) {
        Set<BeleidDto> savedBeleid = new HashSet<>();

        Iterator<BeleidDto> structuurVisieGebiedDtoIterator= beleid.iterator();

        while (structuurVisieGebiedDtoIterator.hasNext()) {
            BeleidDto current = structuurVisieGebiedDtoIterator.next();

            Optional<BeleidDto> found = structuurvisieGebiedBeleidRepository.findByBelangAndRolAndInstrument(current.getBelang(), current.getRol(), current.getInstrument());
            BeleidDto currentStructuurVisieGebiedBeleid = null;
            if (found.isPresent()) {
                currentStructuurVisieGebiedBeleid = found.get();
                currentStructuurVisieGebiedBeleid.setStructuurVisieGebied(savedStructuurVisieGebiedDto);
                currentStructuurVisieGebiedBeleid = structuurvisieGebiedBeleidRepository.save(currentStructuurVisieGebiedBeleid);
            } else {
                current.setStructuurVisieGebied(savedStructuurVisieGebiedDto);
                currentStructuurVisieGebiedBeleid = structuurvisieGebiedBeleidRepository.save(current);
            }
            savedBeleid.add(currentStructuurVisieGebiedBeleid);
        }
        return savedBeleid;
    }

}
