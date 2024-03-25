package pe.gob.muniveintiseisdeoctubre.servicios.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class IncidenceDTO {

    private Integer id;

    private String title;

    private String shortTitle;

    private Integer severity;

    private Date disabledAt;

    private Integer categoryId;

    private String categoryTitle;

    public IncidenceDTO(Integer id, String title, String shortTitle) {
        this.id = id;
        this.title = title;
        this.shortTitle = shortTitle;
    }

    public IncidenceDTO(Integer id, String title, String shortTitle, Integer severity, Date disabledAt, Integer categoryId, String categoryTitle) {
        this.id = id;
        this.title = title;
        this.shortTitle = shortTitle;
        this.severity = severity;
        this.disabledAt = disabledAt;
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
    }
}
