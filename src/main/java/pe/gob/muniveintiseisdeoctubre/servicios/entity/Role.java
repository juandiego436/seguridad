package pe.gob.muniveintiseisdeoctubre.servicios.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    @Access(AccessType.PROPERTY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    public Role(Integer id) {
        this.id = id;
    }
}
