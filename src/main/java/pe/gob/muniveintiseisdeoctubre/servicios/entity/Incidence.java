package pe.gob.muniveintiseisdeoctubre.servicios.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Incidence implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "short_title")
    private String shortTitle;

    @Column(name = "severity")
    private Integer severity;

    @Column(name = "disabledAt")
    private Date disabledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Category category;

    public Incidence(Integer id) {
        this.id = id;
    }
}
