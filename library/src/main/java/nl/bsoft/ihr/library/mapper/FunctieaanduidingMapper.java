package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.Functieaanduiding;
import nl.bsoft.ihr.library.model.dto.FunctieaanduidingDto;
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
public abstract class FunctieaanduidingMapper implements JsonNullableMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "identificatie", source = "id", qualifiedByName = "toFunctieaanduidingId")
    @Mapping(target = "naam", source = "naam")
    @Mapping(target = "labelinfo", source = "labelInfo", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "verwijzingnaartekst", source = "verwijzingNaarTekst", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "styleid", source = "styleId", qualifiedByName = "toJsonNullableString")
    public abstract FunctieaanduidingDto toFunctieaanduiding(Functieaanduiding functieaanduiding) throws ParseException;

    @Named("toFunctieaanduidingId")
    protected String toFunctieaanduidingId(String id) {
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

