package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.Bouwvlak;
import nl.bsoft.ihr.library.model.dto.BouwvlakDto;
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
public abstract class BouwvlakMapper implements JsonNullableMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "identificatie", source = "id", qualifiedByName = "toBouwvlakId")
    @Mapping(target = "naam", source = "naam")
    @Mapping(target = "styleid", source = "styleId", qualifiedByName = "toJsonNullableString")
    public abstract BouwvlakDto toBestemmingsvlak(Bouwvlak bouwvlak) throws ParseException;

    @Named("toBouwvlakId")
    protected String toBestemmingsvlakId(String id) {
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

