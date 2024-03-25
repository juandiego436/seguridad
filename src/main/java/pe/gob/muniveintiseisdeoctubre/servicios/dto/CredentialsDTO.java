package pe.gob.muniveintiseisdeoctubre.servicios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsDTO {
    private String email;
    private String password;
    private String fcmToken;
    private String device;
}
