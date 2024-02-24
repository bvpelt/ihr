package nl.bsoft.ihr.library.mapper;


import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.wololo.geojson.GeoJSON;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class GeoMapper {
    private GeometryFactory geometryFactory;

    @Autowired
    public void setGeometryFactory(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }

    public org.locationtech.jts.geom.Geometry geoJsonToJTS(org.wololo.geojson.GeoJSON geometry) {
        if (geometry == null) {
            return null;
        }
        org.wololo.jts2geojson.GeoJSONReader reader = new org.wololo.jts2geojson.GeoJSONReader();
        return reader.read(geometry, geometryFactory);
    }

    public GeoJSON geoJTSToJson(Geometry geometry) {
        if (geometry == null) {
            return null;
        }
        org.wololo.jts2geojson.GeoJSONWriter reader = new org.wololo.jts2geojson.GeoJSONWriter();
        return reader.write(geometry);
    }
}