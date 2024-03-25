package pe.gob.muniveintiseisdeoctubre.servicios.controller;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.service.EmailService;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.AccountService;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.PasswordResetService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import javax.mail.MessagingException;

@Controller
@RequestMapping("auth")
public class PasswordResetController {

    private static final Log LOG = LogFactory.getLog(PasswordResetController.class);

    @Autowired
    PasswordResetService passwordService;

    @Autowired
    AccountService accountService;

    @Autowired
    private Constants constants;


    @Autowired
    EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/password-resets")
    public ResponseEntity<?> passwordReset(@RequestParam("email") String email) throws MessagingException {
        var password = passwordService.generateCode(email);
        if (password != null) {
            String user = password.getAccount().getFirstName() + " " + password.getAccount().getLastName();
            emailService.sendPasswordReset(email, "Recuperar Contraseña", password.getCode(), user);
        }
        return ResponseEntity.ok(new ResponseDTO<>(null, "Se envio un correo"));
    }

    @GetMapping(value = "/generate-password")
    public String generatePassword(Model model, @RequestParam("code") String code) throws MessagingException {
        var result = passwordService.validateCode(code);
        if (result != null) {
            String newPassword = RandomStringUtils.randomAlphanumeric(6);
            var account = accountService.find(result.getAccount().getId()).get();
            account.setPassword(passwordEncoder.encode(newPassword));
            accountService.createOrUpdate(account);
            emailService.sendPasswordReset(account.getEmail(), "Nueva Contraseña", newPassword, null);
        }
        model.addAttribute("result", result);
        model.addAttribute("logocorreo", constants.baseurl + '/' + constants.mailurl + "logocorreo.png");
        model.addAttribute("resetpass", constants.baseurl + '/' + constants.mailurl + "resetpass.png");
        return "email-validate.html";
    }

}
