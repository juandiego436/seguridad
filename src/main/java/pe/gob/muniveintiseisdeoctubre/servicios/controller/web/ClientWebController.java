package pe.gob.muniveintiseisdeoctubre.servicios.controller.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.AccountDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.AccountService;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.SessionService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import java.util.Date;

@RestController
@RequestMapping("web/users")
public class ClientWebController {
    private static final Log LOG = LogFactory.getLog(ClientWebController.class);

    @Autowired
    private Constants constants;

    @Autowired
    AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    SessionService sessionService;

    @GetMapping
    public ResponseEntity<ResponseDTO> list() {
        var result = accountService.findAllCitizens();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<ResponseDTO> update(@RequestBody AccountDTO body, @PathVariable Integer accountId) {
        var account = accountService.find(accountId).get();
        account.setEmail(body.getEmail());
        account.setFirstName(body.getFirstName());
        account.setLastName(body.getLastName());
        account.getUser().setPhone(body.getUser().getPhone());
        account.getUser().setDni(body.getUser().getDni());
        account.getUser().setAddress(body.getUser().getAddress());
        account.getUser().setType(Constants.UserType.CITIZEN);
        if (body.getBanned() && account.getBannedAt() == null) {
            account.setBannedAt(new Date());
            sessionService.invalidateAll(accountId);
        } else if (!body.getBanned()) {
            account.setBannedAt(null);
        }
        if (body.getPassword() != null && !body.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(body.getPassword()));
        }
        if (body.getPassword() != null && !body.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(body.getPassword()));
        }
        accountService.createOrUpdate(account);
        return ResponseEntity.ok(new ResponseDTO<>());
    }
}
