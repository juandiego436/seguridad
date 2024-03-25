package pe.gob.muniveintiseisdeoctubre.servicios.service;

import java.util.Optional;

public interface CrudService<E, K> {
    E createOrUpdate(E object);

    Optional<E> find(K id);

    Iterable<E> findAll();

    boolean delete(K id);
}