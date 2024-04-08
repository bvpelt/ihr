package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.BaseStructuurvisiegebiedBeleidInner;
import nl.bsoft.ihr.generated.model.IllustratieReferentie;
import nl.bsoft.ihr.generated.model.Structuurvisiegebied;
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
public abstract class StructuurVisieGebiedMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "identificatie", source = "id", qualifiedByName = "toIdentificatie")
    @Mapping(target = "naam", source = "naam")
    @Mapping(target = "themas", source = "thema", qualifiedByName = "toThemaListString")
    @Mapping(target = "beleid", source = "beleid", qualifiedByName = "toBeleidListString")
    @Mapping(target = "verwijzingNaarTekst", source = "verwijzingNaarTekst", qualifiedByName = "toVerwijzingNaarTekstListString")
    @Mapping(target = "illustraties", source = "illustraties", qualifiedByName = "toIllustraties")
    public abstract StructuurVisieGebiedDto toStructuurVisieGebied(Structuurvisiegebied structuurvisiegebied) throws ParseException;

    @Named("toIdentificatie")
    protected String toIdentificatie(String id) {
        return id;
    }

    @Named("toThemaListString")
    protected Set<ThemaDto> toThemaListString(List<String> themas) {
        final Set<ThemaDto> themaDtos = new HashSet<>();

        if ((themas != null) && (themas.size() > 0)) {
            themas.forEach(thema -> {
                ThemaDto themaDto = new ThemaDto();
                themaDto.setThema(thema);
                themaDtos.add(themaDto);
            });
        }
        return themaDtos;
    }

    @Named("toIllustraties")
    protected Set<IllustratieDto>  toIllustraties(List<IllustratieReferentie> illustraties) {

        final Set<IllustratieDto>  illustratieDtos = new HashSet<>();

        if ((illustraties != null) && (illustraties.size() > 0)) {
            illustraties.forEach(illustratie -> {
                IllustratieDto illustratieDto = new IllustratieDto();
                illustratieDto.setHref(illustratie.getHref());
                illustratieDto.setType(illustratie.getType());
                if (illustratie.getNaam().isPresent()) {
                    illustratieDto.setNaam(illustratie.getNaam().get());
                }
                if (illustratie.getLegendanaam().isPresent()) {
                    illustratieDto.setLegendanaam(illustratie.getLegendanaam().get());
                }
                illustratieDtos.add(illustratieDto);
            });
        }
        return illustratieDtos;
    }

    @Named("toVerwijzingNaarTekstListString")
    protected Set<TekstRefDto> toVerwijzingNaarTekstListString(List<String> verwijzingen) {
        final Set<TekstRefDto> tekstRefs = new HashSet<>();

        if ((verwijzingen != null) && (verwijzingen.size() > 0)) {
            verwijzingen.forEach(referentie -> {
                TekstRefDto tekstRef = new TekstRefDto();
                tekstRef.setReferentie(referentie);
                tekstRefs.add(tekstRef);
            });
        }
        return tekstRefs;
    }

    @Named("toBeleidListString")
    protected Set<BeleidDto> toBeleidListString(List<BaseStructuurvisiegebiedBeleidInner> beleiden) {
        final Set<BeleidDto> beleidDtos = new HashSet<>();

        if ((beleiden != null) && (beleiden.size() > 0)) {
            beleiden.forEach(beleid -> {
                BeleidDto beleidDto = new BeleidDto();
                beleidDto.setBelang(toJsonNullableString(beleid.getBelang()));
                beleidDto.setRol(toJsonNullableString(beleid.getRol()));
                beleidDto.setInstrument(toJsonNullableString(beleid.getInstrument()));
                beleidDtos.add(beleidDto);
            });
        }
        return beleidDtos;
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
