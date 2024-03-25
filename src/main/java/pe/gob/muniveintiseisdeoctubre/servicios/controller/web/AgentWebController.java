package pe.gob.muniveintiseisdeoctubre.servicios.controller.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.AccountDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Account;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Client;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.AccountService;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.SessionService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import java.util.Date;

@RestController
@RequestMapping("web/agents")
public class AgentWebController {

    private static final Log LOG = LogFactory.getLog(AgentWebController.class);

    @Autowired
    private Constants constants;

    @Autowired
    AccountService accountService;

    @Autowired
    SessionService sessionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<ResponseDTO> list() {
        var result = accountService.findAllAgents();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@RequestBody AccountDTO body) {
        var account = new Account();
        var user = new Client();

        account.setEmail(body.getEmail());
        account.setFirstName(body.getFirstName());
        account.setLastName(body.getLastName());
        account.setPassword(passwordEncoder.encode(body.getPassword()));

        if (body.getUser().getDni() != null && !body.getUser().getDni().isEmpty()) {
            user.setDni(body.getUser().getDni());
        }

        user.setPhone(body.getUser().getPhone());
        user.setAddress(body.getUser().getAddress());
        user.setType(Constants.UserType.AGENT);
        user.setAccount(account);

        account.setUser(user);
        accountService.createOrUpdate(account);
        return ResponseEntity.ok(new ResponseDTO<>());
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<ResponseDTO> update(@RequestBody AccountDTO body, @PathVariable Integer accountId) {
        final var account = accountService.find(accountId).get();
        account.setEmail(body.getEmail());
        account.setFirstName(body.getFirstName());
        account.setLastName(body.getLastName());
        account.getUser().setPhone(body.getUser().getPhone());
        account.getUser().setDni(body.getUser().getDni());
        account.getUser().setAddress(body.getUser().getAddress());
        if (body.getBanned() && account.getBannedAt() == null) {
            account.setBannedAt(new Date());
            sessionService.invalidateAll(accountId);
        } else if (!body.getBanned()) {
            account.setBannedAt(null);
        }
        if (body.getPassword() != null && !body.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(body.getPassword()));
        }
        accountService.createOrUpdate(account);
        return ResponseEntity.ok(new ResponseDTO<>());
    }
}
