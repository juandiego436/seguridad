package pe.gob.muniveintiseisdeoctubre.servicios.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {
    Optional<Client> findByPhone(String phone);
}
