package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.IllustratieReferentie;
import nl.bsoft.ihr.generated.model.PlanOndergrondenInner;
import nl.bsoft.ihr.library.model.dto.IllustratieDto;
import nl.bsoft.ihr.library.model.dto.OndergrondDto;
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
public abstract class OndergrondMapper implements JsonNullableMapper {

    @Mapping(target = "type", source = "type", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "datum", source = "datum", qualifiedByName = "toJsonNullableString")
    public abstract OndergrondDto toOndergrond(PlanOndergrondenInner planondergrond)  throws ParseException;

    @Named("toJsonNullableString")
    protected String toJsonNullableString(JsonNullable<String> jsonNullable) {
        if (jsonNullable.isPresent()) {
            return jsonNullable.get();
        } else {
            return null;
        }
    }
}

