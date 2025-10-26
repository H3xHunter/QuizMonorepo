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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceMedianoImpl implements IMedianoService {
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
    public MedianoWithPhotosDTO getMedianoWithPhotos(String id) {
        // Get mediano from database
        Mediano mediano = medianoDao.findById(id)
                .orElseThrow(() -> new MedianoNotFoundException(id));

        // Get photos from Photos Service using Feign client
        List<PhotoResponseDTO> photos = photosFeignClient.getPhotosByMedianoId(id);

        // Build response DTO
        return new MedianoWithPhotosDTO(
                mediano.getId(),
                mediano.getName(),
                mediano.getHeight(),
                mediano.getEmail(),
                photos
        );
    }
}

