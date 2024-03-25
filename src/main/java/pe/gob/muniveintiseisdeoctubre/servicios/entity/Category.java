package pe.gob.muniveintiseisdeoctubre.servicios.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "Category")
@Table
@Getter
@Setter
@NoArgsConstructor
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(columnDefinition = "INT(1) UNSIGNED NOT NULL")
    private Integer type;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "disabled_at", columnDefinition = "TIMESTAMP", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date disabledAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "category")
    @JsonIgnore
    private List<Incidence> incidences;

    public Category(Integer id) {
        this.id = id;
    }
}
