package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.Maatvoering;
import nl.bsoft.ihr.generated.model.MaatvoeringCollectie;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.mapper.MaatvoeringMapper;
import nl.bsoft.ihr.library.model.dto.ImroLoadDto;
import nl.bsoft.ihr.library.model.dto.LocatieDto;
import nl.bsoft.ihr.library.model.dto.MaatvoeringDto;
import nl.bsoft.ihr.library.model.dto.OmvangDto;
import nl.bsoft.ihr.library.repository.ImroLoadRepository;
import nl.bsoft.ihr.library.repository.LocatieRepository;
import nl.bsoft.ihr.library.repository.MaatvoeringRepository;
import nl.bsoft.ihr.library.repository.OmvangRepository;
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
public class MaatvoeringService {
    private final APIService APIService;
    private final ImroLoadRepository imroLoadRepository;
    private final MaatvoeringRepository maatvoeringRepository;
    private final OmvangRepository omvangRepository;
    private final LocatieRepository locatieRepository;
    private final MaatvoeringMapper maatvoeringMapper;
    private final LocatieMapper locatieMapper;
    private final int MAXMAATVOERINGEN = 100;

    @Autowired
    public MaatvoeringService(APIService APIService,
                              ImroLoadRepository imroLoadRepository,
                              OmvangRepository omvangRepository,
                              MaatvoeringRepository maatvoeringRepository,
                              LocatieRepository locatieRepository,
                              MaatvoeringMapper maatvoeringMapper,
                              LocatieMapper locatieMapper) {
        this.APIService = APIService;
        this.imroLoadRepository = imroLoadRepository;
        this.omvangRepository = omvangRepository;
        this.maatvoeringRepository = maatvoeringRepository;
        this.locatieRepository = locatieRepository;
        this.maatvoeringMapper = maatvoeringMapper;
        this.locatieMapper = locatieMapper;
    }

    public UpdateCounter loadMaatvoeringenFromList() {
        UpdateCounter updateCounter = new UpdateCounter();
        Iterable<ImroLoadDto> imroLoadDtos = imroLoadRepository.findByIdentificatieNotLoaded();

        imroLoadDtos.forEach(
                imroPlan -> {
                    procesMaatvoering(imroPlan.getIdentificatie(), 1, updateCounter);
                }
        );
        return updateCounter;
    }

    public void procesMaatvoering(String planidentificatie, int page, UpdateCounter updateCounter) {
        MaatvoeringCollectie maatvoeringen = getMaatvoeringenForId(planidentificatie, page);
        if (maatvoeringen != null) {
            saveMaatvoeringen(planidentificatie, page, maatvoeringen, updateCounter);
        }
    }

    private MaatvoeringCollectie getMaatvoeringenForId(String planidentificatie, int page) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/maatvoeringen");
        uriComponentsBuilder.queryParam("pageSize", MAXMAATVOERINGEN);
        uriComponentsBuilder.queryParam("page", page);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), MaatvoeringCollectie.class);
    }

    private void saveMaatvoeringen(String planidentificatie, int page, MaatvoeringCollectie maatvoeringCollectie, UpdateCounter updateCounter) {
        if (maatvoeringCollectie != null) {
            if (maatvoeringCollectie.getEmbedded() != null) {
                if (maatvoeringCollectie.getEmbedded().getMaatvoeringen() != null) {
                    // add each found bouwaanduiding
                    maatvoeringCollectie.getEmbedded().getMaatvoeringen().forEach(maatvoering -> {
                        addMaatvoering(planidentificatie, maatvoering, updateCounter);
                    });
                    // while maximum number of bouwaanduidingen retrieved, get next page
                    if (maatvoeringCollectie.getEmbedded().getMaatvoeringen().size() == MAXMAATVOERINGEN) {
                        procesMaatvoering(planidentificatie, page + 1, updateCounter);
                    }
                }
            }
        }
    }

    @Transactional
    protected MaatvoeringDto addMaatvoering(String planidentificatie, Maatvoering maatvoering, UpdateCounter updateCounter) {
        MaatvoeringDto savedMaatvoering = null;

        try {
            MaatvoeringDto current = maatvoeringMapper.toMaatvoering(maatvoering);
            current.setPlanidentificatie(planidentificatie);
            String md5hash = null;

            if (maatvoering.getGeometrie() != null) {
                md5hash = DigestUtils.md5Hex(maatvoering.getGeometrie().toString().toUpperCase());
                current.setMd5hash(md5hash);

                Optional<LocatieDto> optionalLocatieDto = locatieRepository.findByMd5hash(md5hash);
                if (!optionalLocatieDto.isPresent()) {
                    LocatieDto locatieDto = locatieMapper.toLocatieDto(maatvoering);
                    locatieDto.setMd5hash(md5hash);
                    locatieDto.setRegistratie(LocalDateTime.now());
                    locatieRepository.save(locatieDto);
                    log.debug("Added locatie: {}", md5hash);
                }
            }

            Optional<MaatvoeringDto> optionalFound = maatvoeringRepository.findByPlanidentificatieAndIdentificatie(current.getPlanidentificatie(), current.getIdentificatie());

            if (optionalFound.isPresent()) { // existing entry
                MaatvoeringDto found = optionalFound.get();
                if (found.equals(current)) { // not changed
                    savedMaatvoering = found;
                    updateCounter.skipped();
                } else {                     // changed
                    found.setNaam(current.getNaam());
                    found.setVerwijzingnaartekst(current.getVerwijzingnaartekst());
                    found.setStyleid(current.getStyleid());
                    found.setMd5hash(md5hash);

                    current.getOmvangen().forEach(omvang -> {
                        if (!found.getOmvangen().contains(omvang)) {
                            found.addOmvang(omvang);
                        }
                    });
                    savedMaatvoering = maatvoeringRepository.save(found);
                    updateCounter.updated();
                }
            } else { // new entry
                maatvoering.getOmvang().forEach(omvang -> {
                    String naam = omvang.getNaam();
                    String waarde = omvang.getWaarde();

                    Optional<OmvangDto> optionalOmvang = omvangRepository.findByNaamAndWaarde(naam, waarde);
                    OmvangDto foundOmvang = null;
                    if (optionalOmvang.isPresent()) {
                        foundOmvang = optionalOmvang.get();
                        current.addOmvang(foundOmvang);
                    } else {
                        foundOmvang = new OmvangDto();
                        foundOmvang.setNaam(naam);
                        foundOmvang.setWaarde(waarde);
                        foundOmvang.getMaatvoeringen().add(current);

                        OmvangDto savedOmvang = omvangRepository.save(foundOmvang);
                        current.addOmvang(savedOmvang);
                    }
                });

                savedMaatvoering = maatvoeringRepository.save(current);
                updateCounter.add();
            }
        } catch (Exception e) {
            updateCounter.skipped();
            log.error("Error while processing: {} in maatvoering processing: {}", maatvoering, e);
        }
        return savedMaatvoering;
    }
}
