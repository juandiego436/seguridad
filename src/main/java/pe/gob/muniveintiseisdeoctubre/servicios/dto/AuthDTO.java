package pe.gob.muniveintiseisdeoctubre.servicios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Account;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Session;

@Data
@AllArgsConstructor
public class AuthDTO {
    private Account account;
    private Session session;
}
