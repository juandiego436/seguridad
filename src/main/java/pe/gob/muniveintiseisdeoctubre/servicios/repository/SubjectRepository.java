package pe.gob.muniveintiseisdeoctubre.servicios.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Subject;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Integer> {

}
