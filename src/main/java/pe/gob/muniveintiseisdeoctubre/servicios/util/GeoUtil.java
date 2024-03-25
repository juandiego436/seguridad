package pe.gob.muniveintiseisdeoctubre.servicios.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.Point;

import java.util.List;

@Component
public class GeoUtil {

    private static final Logger LOG = LoggerFactory.getLogger(GeoUtil.class);
    private final ObjectMapper objectMapper;
    private final GeometryFactory geometryFactory;

    public GeoUtil() {
        objectMapper = new ObjectMapper();
        geometryFactory = new GeometryFactory();
    }

    public boolean insidePolygon(String stringPoints, String latitude, String longitude) {
        try {
            List<Point> points = objectMapper.readValue(stringPoints, new TypeReference<>() {
            });
            Coordinate[] coordinates = new Coordinate[points.size()];

            for (int i = 0; i < points.size(); i++) {
                coordinates[i] = (new Coordinate(points.get(i).getLat(), points.get(i).getLng(), 0f));
            }

            Polygon polygon = geometryFactory.createPolygon(coordinates);
            return polygon.contains(geometryFactory.createPoint(new Coordinate(Double.parseDouble(latitude), Double.parseDouble(longitude))));

        } catch (Exception e) {
            return false;
        }
    }
}
