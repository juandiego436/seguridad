package pe.gob.muniveintiseisdeoctubre.servicios.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.AuthDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.CredentialsDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Session;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.AccountService;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.SessionService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    //private static final Log LOG = LogFactory.getLog(CustomAuthenticationProvider.class);
    @Autowired
    private AccountService accountService;

    @Autowired
    SessionService sessionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        final var credentials = (CredentialsDTO) auth.getPrincipal();
        final var account = accountService.findByEmail(credentials.getEmail());

        if (account.isEmpty() || !passwordEncoder.matches(credentials.getPassword(), account.get().getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        entityManager.detach(account.get());
        account.get().setPassword(null);
        Session session = null;
        if (account.get().getBannedAt() == null) {
            if (credentials.getFcmToken() != null) {
                sessionService.invalidateByFcm(credentials.getFcmToken());
            }
            session = sessionService.generate(account.get(), credentials.getFcmToken(), credentials.getDevice());
        }
        final var authAccount = new AuthDTO(account.get(), session);

        return new UsernamePasswordAuthenticationToken(authAccount, credentials.getPassword(), Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}