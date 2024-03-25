package pe.gob.muniveintiseisdeoctubre.servicios.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.AuthDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Account;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Session;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.SessionRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.UUID;

@Service
public class SessionService {

    private static final Logger LOG = LoggerFactory.getLogger(SessionService.class);

    @Autowired
    SessionRepository sessionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Session generate(Account account, String firebaseToken, String device) {
        var calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 6);
        var session = new Session(UUID.randomUUID().toString(), firebaseToken, device, account, calendar.getTime());
        sessionRepository.save(session);
        return session;
    }

    public AuthDTO validate(String token) {
        Session session = sessionRepository.findByToken(token);

        if (session != null && session.getAccount() != null) {
            entityManager.detach(session);
            final var account = session.getAccount();
            session.setAccount(null);
            return new AuthDTO(account, session);
        }
        return null;
    }

    public void invalidate(String token) {
        sessionRepository.removeByToken(token);
    }

    public void invalidateByFcm(String fcmToken) {
        sessionRepository.removeByFcmToken(fcmToken);
    }

    public void invalidateAll(Integer accountId) {
        LOG.debug(accountId.toString());
        sessionRepository.removeByAccountId(accountId);
    }
}
