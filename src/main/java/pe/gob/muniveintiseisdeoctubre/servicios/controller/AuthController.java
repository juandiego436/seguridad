package pe.gob.muniveintiseisdeoctubre.servicios.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.AuthDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.CredentialsDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.SessionService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

@Controller
@RequestMapping("auth")
public class AuthController {

    private static final Log LOG = LogFactory.getLog(AuthController.class);

    @Autowired
    private Constants constants;

    @Autowired
    SessionService sessionService;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody CredentialsDTO credentials, @RequestParam(required = false) String typeAccess) throws AuthenticationException {
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials, null));
        AuthDTO authAccount = (AuthDTO) auth.getPrincipal();

        if (authAccount.getAccount().getBannedAt() != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseDTO<>(null, "Su cuenta ha sido suspendida", constants.buildVersion));
        }

        if (typeAccess != null && typeAccess.equals("ADMIN") && authAccount.getAccount().getAdmin() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO<>(null, "No tiene los permisos requeridos", constants.buildVersion));
        }

        var responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + authAccount.getSession().getToken());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new ResponseDTO<>(authAccount.getAccount(), "ok", constants.buildVersion));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        AuthDTO authAccount = (AuthDTO) auth.getPrincipal();

        sessionService.invalidate(authAccount.getSession().getToken());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ResponseDTO<>(null, "Token invalidado", constants.buildVersion));
    }
}
