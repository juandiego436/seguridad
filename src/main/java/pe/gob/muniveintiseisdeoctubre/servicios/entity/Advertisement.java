package pe.gob.muniveintiseisdeoctubre.servicios.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    @Access(AccessType.PROPERTY)
    private Integer id;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;

    @Column(name = "img_url")
    private String imgUrl;


    @Column(name = "end_date", columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
}
