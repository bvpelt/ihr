package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.Bestemmingsvlak;
import nl.bsoft.ihr.generated.model.Plan;
import nl.bsoft.ihr.generated.model.Structuurvisiegebied;
import nl.bsoft.ihr.library.model.dto.LocatieDto;
import nl.bsoft.ihr.library.service.GeoService;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.mapstruct.*;
import org.wololo.geojson.GeoJSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Setter
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                JsonNullableMapper.class
        },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class LocatieMapper {
    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "geometrie", source = "geometrie", qualifiedByName = "toGeometrie")
    public abstract LocatieDto toLocatieDto(Plan plan) throws ParseException;


    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "geometrie", source = "geometrie", qualifiedByName = "toGeometrie")
    public abstract LocatieDto toLocatieDto(Bestemmingsvlak bestemmingsvlak) throws ParseException;

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "geometrie", source = "geometrie", qualifiedByName = "toGeometrieList")
    public abstract Set<LocatieDto> toLocatieDto(Structuurvisiegebied structuurvisie) throws ParseException;


    @Named("toGeometrie")
    protected Geometry toGeometrie(GeoJSON inputGeometry) throws ParseException {

        GeoService geoService = new GeoService(new GeoMapperImpl());
        return geoService.geoJsonToJTS(inputGeometry);
    }


    @Named("toGeometrieList")
    protected List<Geometry> toGeometrieList(List<GeoJSON> inputGeometries) throws ParseException {

        List<Geometry> geometries = new ArrayList<>();

        inputGeometries.forEach(geojson -> {
            GeoService geoService = new GeoService(new GeoMapperImpl());
            Geometry geometry = geoService.geoJsonToJTS(geojson);
            geometries.add(geometry);
        });
       return geometries;
    }
}
