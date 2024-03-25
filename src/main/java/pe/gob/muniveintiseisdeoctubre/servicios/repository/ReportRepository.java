package pe.gob.muniveintiseisdeoctubre.servicios.repository;

import org.springframework.data.repository.CrudRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Report;

public interface ReportRepository extends CrudRepository<Report, Integer> {
}
