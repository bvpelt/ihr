package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.Lettertekenaanduiding;
import nl.bsoft.ihr.library.model.dto.LettertekenaanduidingDto;
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
public abstract class LettertekenaanduidingMapper implements JsonNullableMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "identificatie", source = "id", qualifiedByName = "toLetteraanduidingId")
    @Mapping(target = "naam", source = "naam")
    @Mapping(target = "labelinfo", source = "labelInfo", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "styleid", source = "styleId", qualifiedByName = "toJsonNullableString")
    public abstract LettertekenaanduidingDto toLettertekenaanduiding(Lettertekenaanduiding letteraanduiding) throws ParseException;

    @Named("toLetteraanduidingId")
    protected String toLetteraanduidingId(String id) {
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

