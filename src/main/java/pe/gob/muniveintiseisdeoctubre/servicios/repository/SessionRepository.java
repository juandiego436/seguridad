package pe.gob.muniveintiseisdeoctubre.servicios.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Session;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
    Session findByToken(String token);

    @Transactional
    void removeByToken(String token);

    @Transactional
    void removeByFcmToken(String fcmToken);

    @Modifying
    @Transactional
    @Query("DELETE from Session s where s.account.id=:accountId")
    void removeByAccountId(Integer accountId);

    @Query("SELECT DISTINCT s.fcmToken FROM Session s where s.fcmToken IS NOT NULL and s.device IN ('android','ios') and s.account.bannedAt IS NULL and s.account.user.type = 'AGENT'")
    ArrayList<String> getActiveTokenAgentsSessions();

    @Query("SELECT DISTINCT s.fcmToken FROM Session s where s.fcmToken IS NOT NULL and s.device IN ('android','ios') and s.account.bannedAt IS NULL and s.account.user.type = 'CITIZEN'")
    ArrayList<String> getActiveTokenCitizenSessions();
}
