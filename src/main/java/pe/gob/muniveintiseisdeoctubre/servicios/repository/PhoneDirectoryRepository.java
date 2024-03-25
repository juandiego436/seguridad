package pe.gob.muniveintiseisdeoctubre.servicios.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.PhoneDirectory;

@Repository
public interface PhoneDirectoryRepository extends CrudRepository<PhoneDirectory, Integer> {

}
