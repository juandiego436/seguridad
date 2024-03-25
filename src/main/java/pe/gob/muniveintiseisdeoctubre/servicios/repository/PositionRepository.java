package pe.gob.muniveintiseisdeoctubre.servicios.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Position;

@Repository
public interface PositionRepository extends CrudRepository<Position, Integer> {
}
