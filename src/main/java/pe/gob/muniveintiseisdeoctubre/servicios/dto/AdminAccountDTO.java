package pe.gob.muniveintiseisdeoctubre.servicios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminAccountDTO {

    private Integer id;

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private Integer roleId;

    private Boolean agentAccess;

    private AdminDTO admin;

    private Boolean banned;

    private Date bannedAt;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", agentAccess=" + agentAccess +
                ", admin=" + admin +
                ", banned=" + banned +
                ", bannedAt=" + bannedAt + +
                '}';
    }
}
