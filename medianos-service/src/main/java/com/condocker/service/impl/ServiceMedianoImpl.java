package com.condocker.service.impl;

import com.condocker.client.PhotosFeignClient;
import com.condocker.dto.MedianoDTO;
import com.condocker.dto.MedianoWithPhotosDTO;
import com.condocker.dto.PhotoResponseDTO;
import com.condocker.entity.Mediano;
import com.condocker.exceptions.DuplicateMedianoException;
import com.condocker.exceptions.MedianoNotFoundException;
import com.condocker.exceptions.NameException;
import com.condocker.repo.IMediano;
import com.condocker.service.IMedianoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceMedianoImpl implements IMedianoService {
    private static final Logger log = LoggerFactory.getLogger(ServiceMedianoImpl.class);

    @Autowired
    private IMediano medianoDao;

    @Autowired
    private PhotosFeignClient photosFeignClient;

    @Override
    public List<MedianoDTO> getMedianos(){
        //SELECT * FROM medianos;
        return medianoDao.findAll().stream().map(
                mediano -> {
                    return new MedianoDTO(
                            mediano.getId(),
                            mediano.getName(),
                            mediano.getHeight(),
                            mediano.getEmail()
                    );
                }
        ).toList();
    }

    @Override
    public Mediano addMediano(MedianoDTO m) {
        Mediano mediano = new Mediano();
        mediano.setHeight(m.altura());
        mediano.setName(m.nombre());
        mediano.setEmail(m.email());
        Mediano busqueda=
                medianoDao.findMedianoByName(mediano.getName())
                        .orElse(null);
        if(busqueda!=null) {
            throw new DuplicateMedianoException(mediano.getName());
        }
        //INSERT INTO MEDIANOS VALUES (nombre,altura)
        return medianoDao.save(mediano);

    }

    @Override
    public Optional<Mediano> removeMediano(String id) {
        Optional<Mediano> mediano = medianoDao.findById(id);
        if (mediano.isPresent()) {
            medianoDao.deleteById(id);
        }
        return mediano;
    }

    @Override
    public Optional<Mediano> getMedianoById(String id) {
        return medianoDao.findById(id);
    }

    @Override
    public Optional<Mediano> getByName(String nombre) {
        return medianoDao.findMedianoByName(nombre);
    }

    @Override
    @Retry(name = "photoService", fallbackMethod = "getMedianoWithPhotosFallback")
    @CircuitBreaker(name = "photoService", fallbackMethod = "getMedianoWithPhotosFallback")
    public MedianoWithPhotosDTO getMedianoWithPhotos(String id) {
        log.info("Attempting to get mediano with photos for ID: {}", id);

        // Get mediano from database
        Mediano mediano = medianoDao.findById(id)
                .orElseThrow(() -> new MedianoNotFoundException(id));

        // Get photos from Photos Service using Feign client
        log.info("Calling Photos Service for mediano ID: {}", id);
        List<PhotoResponseDTO> photos = photosFeignClient.getPhotosByMedianoId(id);

        log.info("Successfully retrieved {} photos for mediano ID: {}", photos.size(), id);

        // Build response DTO
        return new MedianoWithPhotosDTO(
                mediano.getId(),
                mediano.getName(),
                mediano.getHeight(),
                mediano.getEmail(),
                photos
        );
    }

    /**
     * Fallback method for getMedianoWithPhotos when Photos Service is unavailable
     * Returns mediano data without photos
     */
    public MedianoWithPhotosDTO getMedianoWithPhotosFallback(String id, Exception ex) {
        log.warn("Fallback triggered for mediano ID: {}. Reason: {}", id, ex.getMessage());

        // Get mediano from database
        Mediano mediano = medianoDao.findById(id)
                .orElseThrow(() -> new MedianoNotFoundException(id));

        // Return mediano with empty photo list
        log.info("Returning mediano without photos due to Photos Service unavailability");
        return new MedianoWithPhotosDTO(
                mediano.getId(),
                mediano.getName(),
                mediano.getHeight(),
                mediano.getEmail(),
                List.of() // Empty list when Photos Service is down
        );
    }
}

