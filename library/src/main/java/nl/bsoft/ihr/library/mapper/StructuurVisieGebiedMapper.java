package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.Structuurvisiegebied;
import nl.bsoft.ihr.library.model.dto.StructuurVisieGebiedDto;
import org.locationtech.jts.io.ParseException;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                JsonNullableMapper.class
        },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class StructuurVisieGebiedMapper implements JsonNullableMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "identificatie", source = "id", qualifiedByName = "toIdentificatie")
    @Mapping(target = "naam", source = "naam")
    @Mapping(target = "beleid", source = "beleid", ignore = true)
    @Mapping(target = "verwijzingNaarTekst", source = "verwijzingNaarTekst", ignore = true)
    @Mapping(target = "illustraties", source = "illustraties", ignore = true)
    @Mapping(target = "styleid", source = "styleId", qualifiedByName = "toJsonNullableString")

    public abstract StructuurVisieGebiedDto toStructuurVisieGebied(Structuurvisiegebied structuurvisiegebied) throws ParseException;

    @Named("toIdentificatie")
    protected String toIdentificatie(String id) {
        return id;
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
