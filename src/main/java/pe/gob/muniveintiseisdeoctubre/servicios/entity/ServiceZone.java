package pe.gob.muniveintiseisdeoctubre.servicios.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class ServiceZone implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    private Integer id;

    @Column(name = "points", columnDefinition = "TEXT")
    private String points;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "serviceZone")
    private Set<Sector> sectors;

    public ServiceZone(Integer id) {
        this.id = id;
    }
}
