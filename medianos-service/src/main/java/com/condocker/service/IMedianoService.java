package com.condocker.service;


import com.condocker.dto.MedianoDTO;
import com.condocker.dto.MedianoWithPhotosDTO;
import com.condocker.entity.Mediano;

import java.util.List;
import java.util.Optional;

public interface IMedianoService {
    public List<MedianoDTO> getMedianos();
    public Mediano addMediano(MedianoDTO m);
    public Optional<Mediano> removeMediano(String id);
    public Optional<Mediano> getMedianoById(String id);
    public Optional<Mediano> getByName(String nombre);

    /**
     * Get mediano with all associated photos (using Feign client)
     * @param id The mediano's ID
     * @return Mediano with photos
     */
    public MedianoWithPhotosDTO getMedianoWithPhotos(String id);
}
