package pe.gob.muniveintiseisdeoctubre.servicios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Account;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {

    private Integer id;

    private Role role;

    private Account account;
}
