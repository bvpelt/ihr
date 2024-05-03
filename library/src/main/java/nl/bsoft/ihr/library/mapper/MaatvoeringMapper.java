package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.Maatvoering;
import nl.bsoft.ihr.library.model.dto.MaatvoeringDto;
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
public abstract class MaatvoeringMapper implements JsonNullableMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "identificatie", source = "id", qualifiedByName = "toMaatvoeringId")
    @Mapping(target = "naam", source = "naam")
    @Mapping(target = "verwijzingnaartekst", source = "verwijzingNaarTekst", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "styleid", source = "styleId", qualifiedByName = "toJsonNullableString")
    public abstract MaatvoeringDto toMaatvoering(Maatvoering maatvoering) throws ParseException;

    @Named("toMaatvoeringId")
    protected String toMaatvoeringId(String id) {
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

