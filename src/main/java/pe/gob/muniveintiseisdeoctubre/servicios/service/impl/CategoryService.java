package pe.gob.muniveintiseisdeoctubre.servicios.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.CategoryDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.IncidenceDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Category;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.CategoryRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.service.CrudService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements CrudService<Category, Integer> {

    @Autowired
    CategoryRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Category createOrUpdate(Category object) {
        return repository.save(object);
    }

    @Override
    public Optional<Category> find(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Iterable<Category> findAll() {
        return entityManager.createQuery("SELECT c from Category c where c.disabledAt is Null ORDER BY c.sequence ASC", Category.class).getResultList();
    }

    @Transactional
    public List<CategoryDTO> findAllForFilter() {
        var categories = entityManager.createQuery("SELECT c from Category c ORDER BY c.sequence ASC", Category.class).getResultList();
        List<CategoryDTO> result = new ArrayList<>();

        categories.forEach(item -> {
            var category = new CategoryDTO();
            category.setId(item.getId());
            category.setTitle(item.getTitle());
            category.setType(item.getType());
            List<IncidenceDTO> incidences = new ArrayList<>();
            item.getIncidences().forEach(incidence -> {
                incidences.add(new IncidenceDTO(incidence.getId(), incidence.getTitle(), incidence.getShortTitle()));
            });
            category.setIncidences(incidences);
            result.add(category);
        });
        return result;
    }

    @Override
    public boolean delete(Integer id) {
        repository.deleteById(id);
        return true;
    }
}
