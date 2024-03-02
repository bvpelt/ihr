package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.*;
import nl.bsoft.ihr.library.model.dto.TekstDto;
import org.locationtech.jts.io.ParseException;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;


import java.time.LocalDate;

@Setter
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                JsonNullableMapper.class
        },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class TekstMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "tekstidentificatie", source = "id", qualifiedByName = "toId")
    @Mapping(target = "titel", source = "titel", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "inhoud", source = "inhoud", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "volgNummer", source = "volgnummer")
    @Mapping(target = "externHRef", source = "externeReferentie", qualifiedByName = "toExternalHref")
    @Mapping(target = "externLabel", source = "externeReferentie", qualifiedByName = "toExternalLabel")
    public abstract TekstDto toTekst(Tekst tekst) throws ParseException;

    @Named("toId")
    protected String toPlanStatusDate(String id) {
        return id;
    }
    @Named("toExternalHref")
    protected String toExternalHref(JsonNullable<ExterneReferentie> extref) {
        String result = null;

        if (extref.isPresent()) {
            if (extref.get() != null) {
                if (extref.get().getHref().isPresent()) {
                    return extref.get().getHref().get();
                }
            }
        }
        return result;
    }

    @Named("toExternalLabel")
    protected String toExternalLabel(JsonNullable<ExterneReferentie> extref) {
        String result = null;

        if (extref.isPresent()) {
            if (extref.get() != null) {
                if (extref.get().getLabel().isPresent()) {
                    return extref.get().getLabel().get();
                }
            }
        }
        return result;
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
