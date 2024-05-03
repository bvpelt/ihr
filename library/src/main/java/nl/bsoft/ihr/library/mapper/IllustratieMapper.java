package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.IllustratieReferentie;
import nl.bsoft.ihr.library.model.dto.IllustratieDto;
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
public abstract class IllustratieMapper implements JsonNullableMapper {

    @Mapping(target = "href", source = "href")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "naam", source = "naam", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "legendanaam", source = "legendanaam", qualifiedByName = "toJsonNullableString")
    public abstract IllustratieDto toIllustratie(IllustratieReferentie illustratie) throws ParseException;

    @Named("toJsonNullableString")
    protected String toJsonNullableString(JsonNullable<String> jsonNullable) {
        if (jsonNullable.isPresent()) {
            return jsonNullable.get();
        } else {
            return null;
        }
    }
}

