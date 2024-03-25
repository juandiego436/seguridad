package pe.gob.muniveintiseisdeoctubre.servicios.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.SectorStatisticDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Sector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface SectorRepository extends CrudRepository<Sector, Integer> {
    List<Sector> findAll();

    @Query(value="SELECT new pe.gob.muniveintiseisdeoctubre.servicios.dto.SectorStatisticDTO(s.name, c.title, COUNT(r.id)) " +
            "FROM Sector s JOIN Category c on 1=1 " +
            "LEFT JOIN Report r on (c.id = r.category.id and s.id = r.sector.id) " +
            "WHERE ((r.createdAt BETWEEN :startDate and :endDate) OR r.createdAt IS NULL) " +
            "AND (COALESCE(r.incidence.id, 0) IN (:incidences) OR r.incidence.id is NULL) " +
            "AND (COALESCE(r.sector.id, 0) IN (:sectors) OR r.sector.id IS NULL) " +
            "AND (r.status in (:status) OR r.status IS NULL)" +
            "GROUP BY s.id, c.id ORDER BY s.id, c.id ASC")
    ArrayList<SectorStatisticDTO> getSectorStatisticInfo(@Param("startDate") Date startDate,
                                                         @Param("endDate") Date endDate,
                                                         @Param("incidences") Object incidences,
                                                         @Param("sectors") Object sectors,
                                                         @Param("status") ArrayList lStatusFilter);
}
