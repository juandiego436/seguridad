package pe.gob.muniveintiseisdeoctubre.servicios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountDTO {

    private Integer id;

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private ClientDTO user;

    private Boolean agentAccess;

    private Integer roleId;

    private AdminDTO admin;

    private Boolean banned;

    private Date bannedAt;

    private Date createdAt;

    private Date updatedAt;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", user=" + user +
                ", agentAccess=" + agentAccess +
                ", admin=" + admin +
                ", banned=" + banned +
                ", bannedAt=" + bannedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
