package nl.bsoft.ihr.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.generated.model.*;
import nl.bsoft.ihr.library.mapper.LocatieMapper;
import nl.bsoft.ihr.library.mapper.StructuurVisieGebiedMapper;
import nl.bsoft.ihr.library.model.dto.*;
import nl.bsoft.ihr.library.repository.*;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import org.wololo.geojson.GeoJSON;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class StructuurVisieGebiedService {
    private final APIService APIService;
    private final ImroLoadRepository imroLoadRepository;
    private final StructuurvisieGebiedRepository structuurvisieGebiedRepository;
    private final ThemaRepository structuurvisieGebiedThemaRepository;
    private final StructuurvisieGebiedBeleidRepository structuurvisieGebiedBeleidRepository;
    private final TekstRefRepository tekstRefRepository;
    private final BeleidRepository beleidRepository;
    private final ThemaRepository themaRepository;
    private final LocatieRepository locatieRepository;
    private final CartografieRepository cartografieRepository;
    private final IllustratieRepository illustratieRepository;
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
                                       BeleidRepository beleidRepositor,
                                       ThemaRepository themaRepository,
                                       LocatieRepository locatieRepository,
                                       StructuurVisieGebiedMapper structuurVisieGebiedMapper,
                                       CartografieRepository cartografieRepository,
                                       LocatieMapper locatieMapper,
                                       IllustratieRepository illustratieRepository) {
        this.APIService = APIService;
        this.imroLoadRepository = imroLoadRepository;
        this.structuurvisieGebiedRepository = structuurvisieGebiedRepository;
        this.structuurvisieGebiedBeleidRepository = structuurvisieGebiedBeleidRepository;
        this.structuurvisieGebiedThemaRepository = structuurvisieGebiedThemaRepository;
        this.tekstRefRepository = tekstRefRepository;
        this.beleidRepository = beleidRepositor;
        this.themaRepository = themaRepository;
        this.locatieRepository = locatieRepository;
        this.structuurVisieGebiedMapper = structuurVisieGebiedMapper;
        this.cartografieRepository = cartografieRepository;
        this.locatieMapper = locatieMapper;
        this.illustratieRepository = illustratieRepository;
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


    private StructuurvisiegebiedCollectie getStructuurvisiegebiedForId(String planidentificatie, int page) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/plannen/" + planidentificatie + "/structuurvisiegebieden");
        uriComponentsBuilder.queryParam("pageSize", MAXBESTEMMINGSVLAKKEN);
        uriComponentsBuilder.queryParam("page", page);
        log.trace("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), StructuurvisiegebiedCollectie.class);
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

    @Transactional
    protected StructuurVisieGebiedDto addStructuurVisie(String planidentificatie, Structuurvisiegebied structuurvisie, UpdateCounter updateCounter) {
        StructuurVisieGebiedDto savedStructuurVisieGebiedDto = null;
        try {
            StructuurVisieGebiedDto current = structuurVisieGebiedMapper.toStructuurVisieGebied(structuurvisie);
            current.setPlanidentificatie(planidentificatie);

            Set<LocatieDto> geolocaties = new HashSet<>();
            if (structuurvisie.getGeometrie() != null) {
                List<GeoJSON> geometrieen = structuurvisie.getGeometrie();
                geometrieen.forEach(geometrie -> {
                    String md5hash = DigestUtils.md5Hex(structuurvisie.getGeometrie().toString().toUpperCase());
                    //current.setMd5hash(md5hash);
                    //[TODO]
                    Optional<LocatieDto> optionalLocatieDto = locatieRepository.findByMd5hash(md5hash);
                    if (!optionalLocatieDto.isPresent()) {
                        LocatieDto locatieDto = new LocatieDto();
                        locatieDto.setMd5hash(md5hash);
                        locatieDto.setRegistratie(LocalDateTime.now());
                        locatieRepository.save(locatieDto);
                        log.debug("Added locatie: {}", md5hash);
                        geolocaties.add(locatieDto);
                    } else {
                        geolocaties.add(optionalLocatieDto.get());
                    }
                });
            }
            current.setLocaties(geolocaties);

            Optional<StructuurVisieGebiedDto> optionalFound = structuurvisieGebiedRepository.findByPlanidentificatieAndIdentificatie(current.getPlanidentificatie(), current.getIdentificatie());

            if (optionalFound.isPresent()) { // existing entry
                StructuurVisieGebiedDto found = optionalFound.get();
                if (found.equals(current)) { // not changed
                    savedStructuurVisieGebiedDto = found;
                    updateCounter.skipped();
                } else {                     // changed
                    StructuurVisieGebiedDto updated = optionalFound.get();
                    updated.setNaam(current.getNaam());
                    updated.setLocaties(current.getLocaties());

                    savedStructuurVisieGebiedDto = structuurvisieGebiedRepository.save(updated);
                    updateCounter.updated();
                }
            } else { // new
                savedStructuurVisieGebiedDto = structuurvisieGebiedRepository.save(current);
                updateCounter.add();
            }

            // add/update themas
            extractedThema(structuurvisie, savedStructuurVisieGebiedDto);

            // add/update beleid
            extractedBeleid(structuurvisie, savedStructuurVisieGebiedDto);

            // add/update verwijzingNaarTekst
            extractedVerwijzingnaarTekst(structuurvisie, savedStructuurVisieGebiedDto);

            extractedIllustraties(structuurvisie, savedStructuurVisieGebiedDto);
            // [TODO] add/update externeplan_tengevolgevan
            // [TODO] add/update externeplan_gebruiktinformatieuit
            // [TODO] add/update externeplan_uittewerkenin
            // [TODO] add/update externeplan_uitgewerktin

            extractedCartografieInfo(structuurvisie, savedStructuurVisieGebiedDto);
            // [TODO] add/update locaties

            savedStructuurVisieGebiedDto = structuurvisieGebiedRepository.save(savedStructuurVisieGebiedDto);

        } catch (Exception e) {
            log.error("Error while processing: {} in processing: {}", structuurvisie, e);
        }
        return savedStructuurVisieGebiedDto;
    }

    private void extractedCartografieInfo(Structuurvisiegebied structuurvisie, StructuurVisieGebiedDto savedStructuurVisieGebiedDto) {
        List<CartografieInfo> cartografieInfos = structuurvisie.getCartografieInfo();
        Iterator<CartografieInfo> cartografieInfoIterator = cartografieInfos.iterator();
        while (cartografieInfoIterator.hasNext()) {
            CartografieInfo cartografieInfo = cartografieInfoIterator.next();

            int kaartnummer = cartografieInfo.getKaartnummer();
            String kaartnaam = cartografieInfo.getKaartnaam();
            String symboolcode = cartografieInfo.getSymboolCode().isPresent() ? cartografieInfo.getSymboolCode().get() : null;
            Optional<CartografieInfoDto> optionalCartografieInfoDto = cartografieRepository.findByKaartnummerAndKaartnaamAndSymboolcode(kaartnummer, kaartnaam, symboolcode);
            CartografieInfoDto current = null;
            if (optionalCartografieInfoDto.isPresent()) {
                current = optionalCartografieInfoDto.get();
            } else {
                current = new CartografieInfoDto();
                current.setKaartnummer(kaartnummer);
                current.setKaartnaam(kaartnaam);
                current.setSymboolcode(symboolcode);
            }
            current.getStructuurvisiegebied().add(savedStructuurVisieGebiedDto);
            current = cartografieRepository.save(current);
            savedStructuurVisieGebiedDto.getCartografieinfo().add(current);
        }
    }

    private void extractedIllustraties(Structuurvisiegebied structuurvisie, StructuurVisieGebiedDto savedStructuurVisieGebiedDto) {
        List<IllustratieReferentie> illustratieReferenties = structuurvisie.getIllustraties();
        Iterator<IllustratieReferentie> illustraties = illustratieReferenties.iterator();
        while (illustraties.hasNext()) {
            IllustratieReferentie illustratieReferentie = illustraties.next();
            String href = illustratieReferentie.getHref();
            String type = illustratieReferentie.getType();
            String naam = illustratieReferentie.getNaam().isPresent() ? illustratieReferentie.getNaam().get() : null;
            String legendanaam = illustratieReferentie.getLegendanaam().isPresent() ? illustratieReferentie.getLegendanaam().get() : null;
            Optional<IllustratieDto> optionalIllustratieDto = illustratieRepository.findByHrefAndTypeAndNaamAndLegendanaam(href, type, naam, legendanaam);
            IllustratieDto current = null;
            if (optionalIllustratieDto.isPresent()) {
                current = optionalIllustratieDto.get();
            } else {
                current = new IllustratieDto();
                current.setHref(href);
                current.setType(type);
                current.setNaam(naam);
                current.setLegendanaam(legendanaam);
            }
            current.getStructuurvisiegebied().add(savedStructuurVisieGebiedDto);
            current = illustratieRepository.save(current);
            savedStructuurVisieGebiedDto.getIllustraties().add(current);
        }
    }

    private void extractedThema(Structuurvisiegebied structuurvisie, StructuurVisieGebiedDto savedStructuurVisieGebiedDto) {
        List<String> themas = structuurvisie.getThema();
        Iterator<String> themaDtoIterator = themas.iterator();
        while (themaDtoIterator.hasNext()) {
            String thema = themaDtoIterator.next();
            Optional<ThemaDto> optionalThemaDto = themaRepository.findByThema(thema);
            ThemaDto current = null;
            if (optionalThemaDto.isPresent()) {
                current = optionalThemaDto.get();
            } else {
                current = new ThemaDto();
                current.setThema(thema);
            }
            current.getStructuurVisieGebieden().add(savedStructuurVisieGebiedDto);
            current = themaRepository.save(current);
            savedStructuurVisieGebiedDto.getThemas().add(current);
        }
    }

    private void extractedBeleid(Structuurvisiegebied structuurvisie, StructuurVisieGebiedDto savedStructuurVisieGebiedDto) {
        List<BaseStructuurvisiegebiedBeleidInner> beleiden = structuurvisie.getBeleid();

        Iterator<BaseStructuurvisiegebiedBeleidInner> beleidIterator = beleiden.iterator();
        while (beleidIterator.hasNext()) {
            BaseStructuurvisiegebiedBeleidInner beleid = beleidIterator.next();
            String belang = beleid.getBelang().isPresent() ? beleid.getBelang().get() : null;
            String rol = beleid.getRol().isPresent() ? beleid.getRol().get() : null;
            String instrument = beleid.getInstrument().isPresent() ? beleid.getInstrument().get() : null;
            Optional<BeleidDto> optionalBeleidDto = beleidRepository.findByBelangAndRolAndInstrument(belang, rol, instrument);
            BeleidDto current = null;
            if (optionalBeleidDto.isPresent()) {
                current = optionalBeleidDto.get();
            } else {
                current = new BeleidDto();
                current.setBelang(belang);
                current.setRol(rol);
                current.setInstrument(instrument);
            }
            current.getStructuurVisieGebied().add(savedStructuurVisieGebiedDto);
            current = beleidRepository.save(current);
            savedStructuurVisieGebiedDto.getBeleid().add(current);
        }
    }

    private void extractedVerwijzingnaarTekst(Structuurvisiegebied structuurvisie, StructuurVisieGebiedDto savedStructuurVisieGebiedDto) {
        List<String> verwijzingNaarTeksten = structuurvisie.getVerwijzingNaarTekst();
        Iterator<String> verwijzingNaarTekstenIterator = verwijzingNaarTeksten.iterator();
        while (verwijzingNaarTekstenIterator.hasNext()) {
            String verwijzingnaartekst = verwijzingNaarTekstenIterator.next();
            Optional<TekstRefDto> optionalTekstRefDto = tekstRefRepository.findByReferentie(verwijzingnaartekst);
            TekstRefDto current = null;
            if (optionalTekstRefDto.isPresent()) {
                current = optionalTekstRefDto.get();
            } else {
                current = new TekstRefDto();
                current.setReferentie(verwijzingnaartekst);
            }
            current.getVerwijzingNaarTekst().add(savedStructuurVisieGebiedDto);
            current = tekstRefRepository.save(current);
            savedStructuurVisieGebiedDto.getVerwijzingNaarTekst().add(current);
        }
    }
}
