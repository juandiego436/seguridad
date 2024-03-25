package pe.gob.muniveintiseisdeoctubre.servicios.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class PasswordReset implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    private Integer id;

    @Column(name = "code", length = 150, nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Account account;

    @Column(name = "comsumed_at", columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date consumedAt;

    @Column(name = "expires_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    public PasswordReset(String code, Account account, Date consumedAt, Date expiresAt) {
        this.code = code;
        this.account = account;
        this.consumedAt = consumedAt;
        this.expiresAt = expiresAt;
    }
}
