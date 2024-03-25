package pe.gob.muniveintiseisdeoctubre.servicios.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Account;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.AccountRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.service.CrudService;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AccountService implements CrudService<Account, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    AccountRepository repository;

    public Optional<Account> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Account createOrUpdate(Account object) {
        return repository.save(object);
    }

    @Override
    public Optional<Account> find(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Iterable<Account> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public boolean delete(Integer id) {
        repository.deleteById(id);
        return true;
    }

    public Iterable<Account> findAllAgents() {
        return repository.findAllAgents();
    }

    public Iterable<Account> findAllCitizens() {
        return repository.findAllCitizens();
    }

    public Iterable<Account> findAllAdmins() {
        return repository.findAllAdmins();
    }
}
