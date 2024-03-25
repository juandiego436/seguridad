package pe.gob.muniveintiseisdeoctubre.servicios.controller.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.AccountDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.AuthDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Account;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Client;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.AccountService;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.ClientService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RestController
@RequestMapping("app/accounts")
public class AccountController {

    @Autowired
    private Constants constants;

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody AccountDTO accountDTO) {

        if (accountService.findByEmail(accountDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(null, "La dirección de correo ya está en uso", constants.buildVersion));
        }

        if (clientService.findByPhone(accountDTO.getUser().getPhone()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(null, "El número telefónico ya está en uso", constants.buildVersion));
        }
        var account = new Account();
        var user = new Client();
        account.setFirstName(accountDTO.getFirstName());
        account.setLastName(accountDTO.getLastName());
        account.setEmail(accountDTO.getEmail());
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));

        user.setPhone(accountDTO.getUser().getPhone());
        user.setDni(accountDTO.getUser().getDni());
        user.setAddress(accountDTO.getUser().getAddress());
        user.setAccount(account);
        user.setType(Constants.UserType.CITIZEN);

        account.setUser(user);
        accountService.createOrUpdate(account);
        entityManager.detach(account);
        account.setPassword(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(account, "OK", constants.buildVersion));
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<ResponseDTO> changePassword(Authentication authentication, @RequestParam(value = "newPassword") String newPassword) {
        var account = accountService.find(((AuthDTO) authentication.getPrincipal()).getAccount().getId()).get();
        account.setPassword(passwordEncoder.encode(newPassword));
        accountService.createOrUpdate(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(null, "OK", constants.buildVersion));
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<ResponseDTO> deleteAccount(Authentication authentication) {
        var account = accountService.find(((AuthDTO) authentication.getPrincipal()).getAccount().getId()).get();
        accountService.delete(account.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(null, "OK", constants.buildVersion));
    }
}
