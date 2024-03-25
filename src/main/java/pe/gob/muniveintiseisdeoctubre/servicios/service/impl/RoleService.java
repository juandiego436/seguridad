package pe.gob.muniveintiseisdeoctubre.servicios.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Role;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.RoleRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.service.CrudService;

import java.util.Optional;

@Service
public class RoleService implements CrudService<Role, Integer> {

    @Autowired
    RoleRepository repository;

    @Override
    public Role createOrUpdate(Role object) {
        return repository.save(object);
    }

    @Override
    public Optional<Role> find(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Iterable<Role> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean delete(Integer id) {
        repository.deleteById(id);
        return true;
    }
}
