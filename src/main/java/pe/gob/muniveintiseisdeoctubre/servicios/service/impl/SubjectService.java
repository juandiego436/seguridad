package pe.gob.muniveintiseisdeoctubre.servicios.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Subject;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.SubjectRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.service.CrudService;

import java.util.Optional;

@Service
public class SubjectService implements CrudService<Subject, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(SubjectService.class);

    @Autowired
    SubjectRepository repository;

    @Override
    public Subject createOrUpdate(Subject object) {
        return repository.save(object);
    }

    @Override
    public Optional<Subject> find(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Iterable<Subject> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean delete(Integer id) {
        repository.deleteById(id);
        return true;
    }

}
