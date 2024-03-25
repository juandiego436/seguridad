package pe.gob.muniveintiseisdeoctubre.servicios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Account;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    private Integer id;

    private Account account;

    private String phone;

    private String dni;

    private String address;

    private Constants.UserType type;

    private Date createdAt;

    private Date updatedAt;

    public ClientDTO(Integer id) {
        this.id = id;
    }
}
