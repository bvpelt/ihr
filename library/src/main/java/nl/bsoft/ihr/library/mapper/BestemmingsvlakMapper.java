package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.Bestemmingsvlak;
import nl.bsoft.ihr.library.model.dto.BestemmingsvlakDto;
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
public abstract class BestemmingsvlakMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "identificatie", source = "id", qualifiedByName = "toId")
    @Mapping(target = "type", source = "type", qualifiedByName = "toType")
    @Mapping(target = "naam", source = "naam")
    @Mapping(target = "bestemmingshoofdgroep", source = "bestemmingshoofdgroep", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "artikelnummer", source = "artikelnummer", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "labelInfo", source = "labelInfo", qualifiedByName = "toJsonNullableString")
    public abstract BestemmingsvlakDto toBestemmingsvlak(Bestemmingsvlak bestemmingsvlak) throws ParseException;

    @Named("toId")
    protected String toPlanStatusDate(String id) {
        return id;
    }

    @Named("toType")
    protected String toType(Bestemmingsvlak.TypeEnum typeEnum) {
        return typeEnum.getValue();
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

