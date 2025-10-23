package com.condocker.service;


import com.condocker.dto.MedianoDTO;
import com.condocker.entity.Mediano;

import java.util.List;
import java.util.Optional;

public interface IMedianoService {
    public List<MedianoDTO> getMedianos();
    public Mediano addMediano(MedianoDTO m);
    public Optional<Mediano> removeMediano(String id);
    public Optional<Mediano> getMedianoById(String id);
    public Optional<Mediano> getByName(String nombre);
    //public List<Photo> getPhotosMediano(String id);
}
