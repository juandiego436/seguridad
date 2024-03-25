package pe.gob.muniveintiseisdeoctubre.servicios.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
public class HeatMapDataDTO {

    HashMap<String, String> location;
    private double weight;

    public HeatMapDataDTO(String latitude, String longitude, double weight) {
        final var location = new HashMap<String, String>();
        location.put("lat", latitude);
        location.put("lng", longitude);
        this.location = location;
        this.weight = weight;
    }
}
