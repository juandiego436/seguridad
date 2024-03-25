package pe.gob.muniveintiseisdeoctubre.servicios.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "PhoneDirectory")
@Table
@NoArgsConstructor
@Getter
@Setter
public class PhoneDirectory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    private Integer id;

    @Column(length = 150, nullable = false)
    private String title;

    @Column(length = 15, nullable = false)
    private String number;
}
