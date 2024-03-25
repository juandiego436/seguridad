package pe.gob.muniveintiseisdeoctubre.servicios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Integer id;

    private String title;

    private Integer type;

    private List<IncidenceDTO> incidences;
}
