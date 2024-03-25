package pe.gob.muniveintiseisdeoctubre.servicios.controller.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.AccountDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.AdminAccountDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Account;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Admin;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Client;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Role;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.RoleRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.AccountService;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.ClientService;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.SessionService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@RestController
@RequestMapping("web/admins")
public class AdminWebController {

    private static final Log LOG = LogFactory.getLog(AdminWebController.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Constants constants;

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    SessionService sessionService;

    @GetMapping
    public ResponseEntity<ResponseDTO> list() {
        var result = accountService.findAllAdmins();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@RequestBody AdminAccountDTO body) {
        LOG.debug(body);
        var account = new Account();

        account.setEmail(body.getEmail());
        account.setFirstName(body.getFirstName());
        account.setLastName(body.getLastName());
        account.setPassword(passwordEncoder.encode(body.getPassword()));
        account.setAdmin(new Admin(account, new Role(body.getRoleId())));

        if (body.getAgentAccess() != null && body.getAgentAccess()) {
            var user = new Client();
            user.setType(Constants.UserType.AGENT);
            user.setAccount(account);
            account.setUser(user);
        } else {
            account.setUser(null);
        }

        accountService.createOrUpdate(account);
        return ResponseEntity.ok(new ResponseDTO<>());
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<ResponseDTO> update(@RequestBody AccountDTO body, @PathVariable Integer accountId) {
        LOG.debug(body);
        var account = accountService.find(accountId).get();
        account.setEmail(body.getEmail());
        account.setFirstName(body.getFirstName());
        account.setLastName(body.getLastName());

        if (body.getRoleId() != null) {
            account.getAdmin().setRole(entityManager.getReference(Role.class, body.getRoleId()));
        }

        if (body.getAgentAccess() != null && body.getAgentAccess() && account.getUser() == null) {
            var user = new Client();
            user.setAccount(account);
            user.setType(Constants.UserType.AGENT);
            user.setPhone(null);
            user.setAddress(null);
            account.setUser(user);
        } else {
            account.setUser(null);
        }

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
