package pe.gob.muniveintiseisdeoctubre.servicios.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.IncidenceDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Incidence;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.IncidenceRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.service.CrudService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class IncidenceService implements CrudService<Incidence, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(IncidenceService.class);

    @Autowired
    IncidenceRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Incidence> findByCategoryId(Integer categoryId) {
        return entityManager.createQuery("SELECT i " +
                        "FROM Incidence i where i.disabledAt IS NULL AND i.category.id = :categoryId ORDER BY i.title ASC", Incidence.class)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }

    public List<IncidenceDTO> findAllFull() {
        return entityManager.createQuery("SELECT NEW pe.gob.muniveintiseisdeoctubre.servicios.dto.IncidenceDTO(i.id, i.title, i.shortTitle, i.severity, i.disabledAt, i.category.id, i.category.title) " +
                        "FROM Incidence i INNER JOIN i.category c ORDER BY i.category.sequence, i.title ASC", IncidenceDTO.class)
                .getResultList();
    }

    @Override
    public Incidence createOrUpdate(Incidence object) {
        return repository.save(object);
    }

    @Override
    public Optional<Incidence> find(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Iterable<Incidence> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean delete(Integer id) {
        repository.deleteById(id);
        return true;
    }
}
