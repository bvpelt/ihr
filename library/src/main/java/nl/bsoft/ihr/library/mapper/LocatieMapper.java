package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.*;
import nl.bsoft.ihr.library.model.dto.LocatieDto;
import nl.bsoft.ihr.library.service.GeoService;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.mapstruct.*;
import org.wololo.geojson.GeoJSON;


@Setter
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                JsonNullableMapper.class
        },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class LocatieMapper implements JsonNullableMapper {
    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "geometrie", source = "geometrie", qualifiedByName = "toGeometrie")
    public abstract LocatieDto toLocatieDto(Plan plan) throws ParseException;


    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "geometrie", source = "geometrie", qualifiedByName = "toGeometrie")
    public abstract LocatieDto toLocatieDto(Bestemmingsvlak bestemmingsvlak) throws ParseException;

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "geometrie", source = "geometrie", qualifiedByName = "toGeometrie")
    public abstract LocatieDto toLocatieDto(Bouwvlak bouwvlak) throws ParseException;

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "geometrie", source = "geometrie", qualifiedByName = "toGeometrie")
    public abstract LocatieDto toLocatieDto(Functieaanduiding functieaanduiding) throws ParseException;

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "geometrie", source = "geometrie", qualifiedByName = "toGeometrie")
    public abstract LocatieDto toLocatieDto(Bouwaanduiding bouwaanduiding) throws ParseException;

    @Named("toGeometrie")
    protected Geometry toGeometrie(GeoJSON inputGeometry) throws ParseException {

        GeoService geoService = new GeoService(new GeoMapperImpl());
        return geoService.geoJsonToJTS(inputGeometry);
    }

}
