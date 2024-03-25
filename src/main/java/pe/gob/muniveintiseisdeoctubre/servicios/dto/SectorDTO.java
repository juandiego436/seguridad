package pe.gob.muniveintiseisdeoctubre.servicios.dto;

import lombok.Data;


@Data
public class SectorDTO {

    private Integer id;

    private String name;

    private String description;

    private String points;

    private Integer serviceZoneId;

}
