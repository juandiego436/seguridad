package pe.gob.muniveintiseisdeoctubre.servicios.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Client;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.ClientRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.service.CrudService;

import java.util.Optional;

@Service
public class ClientService implements CrudService<Client, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    ClientRepository repository;

    public Optional<Client> findByPhone(String phone) {
        return repository.findByPhone(phone);
    }

    @Override
    public Client createOrUpdate(Client object) {
        return repository.save(object);
    }

    @Override
    public Optional<Client> find(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Iterable<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean delete(Integer id) {
        repository.deleteById(id);
        return true;
    }
}
