package pe.gob.muniveintiseisdeoctubre.servicios.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Incidence;

import java.util.List;

@Repository
public interface IncidenceRepository extends CrudRepository<Incidence, Integer> {
    List<Incidence> findByCategoryId(Integer categoryId);
}
