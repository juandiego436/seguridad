package pe.gob.muniveintiseisdeoctubre.servicios.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "Report")
@Table
@Getter
@Setter
@NoArgsConstructor
public class Report implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sector_id", referencedColumnName = "id")
    private Sector sector;

    @Column(length = 24)
    private String latitude;

    @Column(length = 24)
    private String longitude;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id")
    private Incidence incidence;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "photo_url", columnDefinition = "TEXT")
    private String photoUrl;

    @Column(length = 30, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Constants.ReportStatus status = Constants.ReportStatus.PENDING;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;
}
