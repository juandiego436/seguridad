package pe.gob.muniveintiseisdeoctubre.servicios.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "Account")
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "fist_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(length = 150, nullable = false)
    @JsonIgnore
    private String password;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Client user;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Admin admin;

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions;

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports;

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PasswordReset> passwordResets;

    @Column(name = "banned_at", columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bannedAt;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;

    public Account(Integer id) {
        this.id = id;
    }
}
