package pe.gob.muniveintiseisdeoctubre.servicios.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Advertisement;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.AdvertisementRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.SessionRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.service.CrudService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdvertisementService implements CrudService<Advertisement, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(AdvertisementService.class);

    @Autowired
    AdvertisementRepository repository;

    @Autowired
    private SessionRepository sessionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Constants constants;

    private final ObjectMapper objectMapper;

    public AdvertisementService() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public Advertisement createOrUpdate(Advertisement object) {
        return repository.save(object);
    }

    public Advertisement register(String url, String endDate, MultipartFile file) throws IOException, ParseException {
        var advertisement = new Advertisement();
        advertisement.setUrl(url);
        advertisement.setEndDate(new Date(Long.parseLong(endDate)));
        advertisement.setImgUrl(writeFileAndGetPath(file));
        advertisement = repository.save(advertisement);

        final var registrationTokens = sessionRepository.getActiveTokenCitizenSessions();
        if (registrationTokens != null && !registrationTokens.isEmpty()) {
            sendAdvertisementNotification(advertisement, registrationTokens);
        }

        return advertisement;
    }

    public Advertisement update(Integer advertisementId, Advertisement advertisementModified, MultipartFile file) throws IOException, ParseException {
        var advertisement = repository.findById(advertisementId);
        if (!advertisement.isPresent()) {
            throw new NoResultException("Advertisement id: " + advertisementId + " not found");
        }
        advertisement.get().setUrl(advertisementModified.getUrl());
        advertisement.get().setEndDate(advertisementModified.getEndDate());
        if (file != null && !file.isEmpty()) {
            advertisement.get().setImgUrl(writeFileAndGetPath(file));
        }
        return repository.save(advertisement.get());
    }

    @Override
    public Optional<Advertisement> find(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Iterable<Advertisement> findAll() {
        return repository.findAll();
    }

    public Iterable<Advertisement> findActives() {
        try {
            return entityManager.createQuery("SELECT AD FROM Advertisement AD where AD.endDate >= CURRENT_TIMESTAMP ORDER BY AD.id DESC", Advertisement.class).getResultList();
        } catch (NoResultException exception) {
            return null;
        }
    }

    public Advertisement last() {
        try {
            return entityManager.createQuery("SELECT AD FROM Advertisement AD ORDER BY AD.id DESC", Advertisement.class).setMaxResults(1).getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    public Advertisement lastActive() {
        try {
            return entityManager.createQuery("SELECT AD FROM Advertisement AD where AD.endDate >= CURRENT_TIMESTAMP ORDER BY AD.id DESC", Advertisement.class).setMaxResults(1).getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    @Override
    public boolean delete(Integer id) {
        repository.deleteById(id);
        return true;
    }

    private String writeFileAndGetPath(MultipartFile file) throws IOException {
        if (file != null) {
            final var bytes = file.getBytes();
            String fileName = UUID.nameUUIDFromBytes(bytes) + "." + file.getOriginalFilename().split("\\.")[1];
            final var path = Paths.get(constants.uploadAdvertisementPath + fileName);
            Files.write(path, bytes);

            return constants.uploadAdvertisementUrl + fileName;
        }
        return null;
    }

    private void sendAdvertisementNotification(Advertisement advertisement, ArrayList<String> registrationTokens) {
        MulticastMessage message = null;
        try {
            message = MulticastMessage.builder()
                    .putData("data", objectMapper.writeValueAsString(advertisement))
                    .putData("title", "Publicidad " + constants.nameMunicipalidad)
                    .putData("condition", "false")
                    .putData("body", "Se generó una nueva publicidad")
                    .addAllTokens(registrationTokens)
                    .build();
            final var response = FirebaseMessaging.getInstance().sendMulticast(message);
            LOG.info(response.getSuccessCount() + " messages were sent successfully");
            LOG.warn(response.getFailureCount() + " messages didn't sent successfully");
        } catch (JsonProcessingException | FirebaseMessagingException | IllegalStateException e) {
            LOG.error(e.getMessage());
        }
    }

}
