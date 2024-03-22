package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.BaseBestemmingsvlakBestemmingsfunctiesInner;
import nl.bsoft.ihr.generated.model.Gebiedsaanduiding;
import nl.bsoft.ihr.library.model.dto.*;
import org.locationtech.jts.io.ParseException;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                JsonNullableMapper.class
        },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class GebiedsaanduidingMapper implements JsonNullableMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "identificatie", source = "id", qualifiedByName = "toIdentificatie")
    @Mapping(target = "naam", source = "naam")
    @Mapping(target = "gebiedsaanduidinggroep", source = "gebiedsaanduidinggroep", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "artikelnummers", source = "artikelnummers", qualifiedByName = "toArtikelnummers")
    @Mapping(target = "verwijzingNaarTekst", source = "verwijzingNaarTekst", qualifiedByName = "toVerwijzingNaarTekst")
    @Mapping(target = "labelinfo", source = "labelInfo", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "bestemmingfuncties", source = "bestemmingsfuncties", qualifiedByName = "toBestemmingsfunctie")
    public abstract GebiedsaanduidingDto toGebiedsaanduiding(Gebiedsaanduiding gebiedsaanduiding) throws ParseException;
    @Named("toIdentificatie")
    protected String toIdentificatie(String id) {
        return id;
    }
    @Named("toLabelInfo")
    protected String toLabelInfo(Gebiedsaanduiding gebiedsaanduiding) {
        if (gebiedsaanduiding.getLabelInfo().isPresent()) {
            return gebiedsaanduiding.getLabelInfo().get();
        } else {
            return null;
        }
    }
    @Named("toThemaListString")
    protected Set<StructuurVisieGebiedThemaDto> toThemaListString(List<String> themas) {
        final Set<StructuurVisieGebiedThemaDto> themaDtos = new HashSet<>();

        if ((themas != null) && (themas.size() > 0)) {
            themas.forEach(thema -> {
                StructuurVisieGebiedThemaDto themaDto = new StructuurVisieGebiedThemaDto();
                themaDto.setThema(thema);
                themaDtos.add(themaDto);
            });
        }
        return themaDtos;
    }
    @Named("toArtikelnummers")
    protected Set<ArtikelnummerRefDto> toArtikelnummers(List<String> artikelen) {
        final Set<ArtikelnummerRefDto> artkelRefs = new HashSet<>();

        if ((artikelen != null) && (artikelen.size() > 0)) {
            artikelen.forEach(artikel -> {
                ArtikelnummerRefDto artikelnummerRefDto = new ArtikelnummerRefDto();
                artikelnummerRefDto.setArtikel(artikel);
                artkelRefs.add(artikelnummerRefDto);
            });
        }
        return artkelRefs;
    }
    @Named("toVerwijzingNaarTekst")
    protected Set<TekstRefDto> toVerwijzingNaarTekst(List<String> verwijzingen) {
        final Set<TekstRefDto> tekstRefDtos = new HashSet<>();

        if ((verwijzingen != null) && (verwijzingen.size() > 0)) {
            verwijzingen.forEach(verwijzing -> {
                TekstRefDto tekstRefDto = new TekstRefDto();
                tekstRefDto.setReferentie(verwijzing);
                tekstRefDtos.add(tekstRefDto);
            });
        }
        return tekstRefDtos;
    }
    @Named("toBestemmingsfunctie")
    protected Set<BestemmingFunctieDto> toBestemmingsfuncties(List<BaseBestemmingsvlakBestemmingsfunctiesInner> bestemmingsfuncties) {
        final Set<BestemmingFunctieDto> bestemmingFunctieDtos = new HashSet<>();

        if (bestemmingsfuncties != null) {
            bestemmingsfuncties.forEach(bestemmingsfunctie -> {
                BestemmingFunctieDto bestemmingFunctieDto = new BestemmingFunctieDto();
                bestemmingFunctieDto.setBestemmingsfunctie(bestemmingsfunctie.getBestemmingsfunctie());
                bestemmingFunctieDto.setFunctieniveau(bestemmingsfunctie.getFunctieniveau());
                bestemmingFunctieDtos.add(bestemmingFunctieDto);
            });
        }
        return bestemmingFunctieDtos;
    }
    @Named("toJsonNullableString")
    protected String toJsonNullableString(JsonNullable<String> jsonNullable) {
        if (jsonNullable.isPresent()) {
            return jsonNullable.get();
        } else {
            return null;
        }
    }
}
