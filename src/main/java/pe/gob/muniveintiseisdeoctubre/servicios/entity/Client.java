package pe.gob.muniveintiseisdeoctubre.servicios.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "User")
@Table(name = "user")
@NoArgsConstructor
@Getter
@Setter
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    @Access(AccessType.PROPERTY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Account account;

    @Column(length = 15, unique = true)
    private String phone;

    @Column(length = 8, unique = true)
    private String dni;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 15)
    @Enumerated(value = EnumType.STRING)
    private Constants.UserType type;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @JsonIgnore
    private Date createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @JsonIgnore
    private Date updatedAt;
}
