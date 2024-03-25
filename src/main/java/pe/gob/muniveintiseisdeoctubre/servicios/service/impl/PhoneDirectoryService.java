package pe.gob.muniveintiseisdeoctubre.servicios.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.PhoneDirectory;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.PhoneDirectoryRepository;

@Service
public class PhoneDirectoryService {

    @Autowired
    PhoneDirectoryRepository repository;

    public Iterable<PhoneDirectory> list() {
        return repository.findAll();
    }
}
