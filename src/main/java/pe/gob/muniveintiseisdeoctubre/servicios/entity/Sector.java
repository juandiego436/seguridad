package pe.gob.muniveintiseisdeoctubre.servicios.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.SectorDTO;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Sector")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sector implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    private Integer id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "points", columnDefinition = "TEXT")
    private String points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_zone_id", nullable = false)
    @JsonIgnore
    private ServiceZone serviceZone;

    public Sector(SectorDTO sectorDTO) {
        this.id = sectorDTO.getId();
        this.name = sectorDTO.getName();
        this.description = sectorDTO.getDescription();
        this.points = sectorDTO.getPoints();
        this.serviceZone = new ServiceZone(sectorDTO.getServiceZoneId());
    }
}
