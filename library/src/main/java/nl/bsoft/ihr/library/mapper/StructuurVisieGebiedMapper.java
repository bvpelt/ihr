package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.*;
import nl.bsoft.ihr.library.model.dto.StructuurVisieGebiedBeleidDto;
import nl.bsoft.ihr.library.model.dto.StructuurVisieGebiedDto;
import nl.bsoft.ihr.library.model.dto.StructuurVisieGebiedThemaDto;
import nl.bsoft.ihr.library.model.dto.TekstRefDto;
import org.locationtech.jts.io.ParseException;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.ArrayList;
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
    @Mapping(target = "thema", source = "thema", qualifiedByName = "toThemaListString")
    @Mapping(target = "beleid", source = "beleid", qualifiedByName = "toBeleidListString")
    @Mapping(target = "verwijzingNaarTekst", source = "verwijzingNaarTekst", qualifiedByName = "toVerwijzingNaarTekstListString")
    public abstract StructuurVisieGebiedDto toStructuurVisieGebied(Structuurvisiegebied structuurvisiegebied) throws ParseException;
    @Named("toIdentificatie")
    protected String toIdentificatie(String id) {
        return id;
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
    protected Set<StructuurVisieGebiedBeleidDto> toBeleidListString(List<BaseStructuurvisiegebiedBeleidInner> beleiden) {
        final Set<StructuurVisieGebiedBeleidDto> beleidDtos =new HashSet<>();

        if ((beleiden != null) && (beleiden.size() > 0)) {
            beleiden.forEach(beleid -> {
                StructuurVisieGebiedBeleidDto beleidDto = new StructuurVisieGebiedBeleidDto();
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
