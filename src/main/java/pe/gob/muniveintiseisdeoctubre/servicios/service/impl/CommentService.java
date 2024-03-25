package pe.gob.muniveintiseisdeoctubre.servicios.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Comment;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.CommentRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.service.CrudService;

import java.util.Optional;

@Service
public class CommentService implements CrudService<Comment, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    CommentRepository repository;

    @Override
    public Comment createOrUpdate(Comment object) {
        return repository.save(object);
    }

    @Override
    public Optional<Comment> find(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Iterable<Comment> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean delete(Integer id) {
        repository.deleteById(id);
        return true;
    }


}
