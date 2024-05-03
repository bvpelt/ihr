package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.Figuur;
import nl.bsoft.ihr.library.model.dto.FiguurDto;
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
public abstract class FiguurMapper implements JsonNullableMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "artikelnummers", source = "artikelnummers", ignore = true)
    @Mapping(target = "illustraties", source = "illustraties", ignore = true)
    @Mapping(target = "identificatie", source = "id", qualifiedByName = "toFiguurId")
    @Mapping(target = "naam", source = "naam")
    @Mapping(target = "labelinfo", source = "labelInfo", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "styleid", source = "styleId", qualifiedByName = "toJsonNullableString")
    public abstract FiguurDto toMaatvoering(Figuur figuur) throws ParseException;

    @Named("toFiguurId")
    protected String toFiguurId(String id) {
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

