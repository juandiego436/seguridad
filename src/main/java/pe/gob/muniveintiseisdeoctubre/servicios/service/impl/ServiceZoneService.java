package pe.gob.muniveintiseisdeoctubre.servicios.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.ServiceZone;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class ServiceZoneService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceZoneService.class);

    @PersistenceContext
    private EntityManager entityManager;


    public ServiceZone getFirst() {
        try {
            return entityManager.createQuery("SELECT s FROM ServiceZone s", ServiceZone.class)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
