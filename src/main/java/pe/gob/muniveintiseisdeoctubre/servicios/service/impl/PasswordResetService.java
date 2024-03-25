package pe.gob.muniveintiseisdeoctubre.servicios.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Account;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.PasswordReset;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.AccountRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.PasswordResetRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetService.class);

    @Autowired
    PasswordResetRepository repository;

    @Autowired
    AccountRepository accountRepository;

    public PasswordReset generateCode(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isPresent()) {
            String code = UUID.randomUUID().toString();
            var fecha = Calendar.getInstance();
            fecha.add(Calendar.HOUR, 24);
            var passwordReset = new PasswordReset(code, account.get(), null, fecha.getTime());
            repository.save(passwordReset);
            return passwordReset;
        }
        return null;
    }

    public PasswordReset validateCode(String code) {
        Date fecha = new Date();
        var result = repository.findByCode(code).get();
        if (result.getConsumedAt() == null && result.getExpiresAt().getTime() > fecha.getTime()) {
            result.setConsumedAt(fecha);
            repository.save(result);
            return result;
        }

        return null;
    }
}
