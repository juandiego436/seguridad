package pe.gob.muniveintiseisdeoctubre.servicios.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.SectorDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.SectorStatisticDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Sector;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.SectorRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.service.CrudService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import java.util.*;

@Service
public class SectorService implements CrudService<Sector, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(SubjectService.class);

    @Autowired
    SectorRepository repository;

    @Override
    public Sector createOrUpdate(Sector object) {
        return repository.save(object);
    }

    public Sector createOrUpdate(SectorDTO object) {
        return repository.save(new Sector(object));
    }

    @Override
    public Optional<Sector> find(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<Sector> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean delete(Integer id) {
        repository.deleteById(id);
        return true;
    }

    public List<SectorStatisticDTO> sectorCategoryStatistics(Map<String, Object> filters) {
        final var lStatusFilter = new ArrayList<>();
        ((List<String>) filters.get("status")).forEach(item -> {
            lStatusFilter.add(Constants.ReportStatus.valueOf(item));
        });

        return repository.getSectorStatisticInfo(new Date((Long) filters.get("startDate")),
                new Date((Long) filters.get("endDate")),
                filters.get("incidences"),
                filters.get("sectors"),
                lStatusFilter);
    }
}
