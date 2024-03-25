package pe.gob.muniveintiseisdeoctubre.servicios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<E> implements Serializable {
    private E data;
    private String message;
    private String apiVersion;

    public ResponseDTO(E data, String message) {
        this.data = data;
        this.message = message;
    }

}
