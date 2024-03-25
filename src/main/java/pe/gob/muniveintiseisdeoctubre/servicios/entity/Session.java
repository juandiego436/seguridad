package pe.gob.muniveintiseisdeoctubre.servicios.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "Session")
@Table
@Getter
@Setter
@NoArgsConstructor
public class Session implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    private Integer id;

    @Column(length = 50, nullable = false, unique = true)
    private String token;

    @Column(name = "fcm_token", length = 192)
    private String fcmToken;

    @Column(name = "device", length = 25)
    private String device;

    @ManyToOne(optional = false)
    private Account account;

    @Column(name = "expires_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    public Session(String token, String fcmToken, String device, Account account, Date expiredAt) {
        this.token = token;
        this.fcmToken = fcmToken;
        this.device = device;
        this.account = account;
        this.expiresAt = expiredAt;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", fcmToken='" + fcmToken + '\'' +
                ", device='" + device + '\'' +
                ", account=" + account +
                ", expiresAt=" + expiresAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
