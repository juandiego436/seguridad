package pe.gob.muniveintiseisdeoctubre.servicios.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Position;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.PositionRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.service.CrudService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Optional;

@Service
public class PositionService implements CrudService<Position, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(PositionService.class);

    @Autowired
    PositionRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Position createOrUpdate(Position object) {
        return repository.save(object);
    }

    @Override
    public Optional<Position> find(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Iterable<Position> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    public Iterable<Position> getLastHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -2);

        return entityManager.createQuery("SELECT p FROM Position p where p.updatedAt > ?1 ORDER BY p.accountId DESC", Position.class)
                .setParameter(1, calendar.getTime())
                .getResultList();
    }

}
