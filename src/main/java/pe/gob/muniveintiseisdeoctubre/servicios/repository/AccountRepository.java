package pe.gob.muniveintiseisdeoctubre.servicios.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);

    @Query("SELECT a FROM Account a where a.user.type = 'AGENT' and NOT EXISTS (select 1 from Admin ad where a.id = ad.account.id)")
    Iterable<Account> findAllAgents();

    @Query("SELECT a FROM Account a where a.user.type = 'CITIZEN' and NOT EXISTS (select 1 from Admin ad where a.id = ad.account.id)")
    Iterable<Account> findAllCitizens();

    @Query("SELECT DISTINCT acc FROM Admin a INNER JOIN a.account acc")
    Iterable<Account> findAllAdmins();
}
