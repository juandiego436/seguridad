package pe.gob.muniveintiseisdeoctubre.servicios.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.PasswordReset;

import java.util.Optional;

@Repository
public interface PasswordResetRepository extends CrudRepository<PasswordReset, Integer> {
    Optional<PasswordReset> findByCode(String code);
}
