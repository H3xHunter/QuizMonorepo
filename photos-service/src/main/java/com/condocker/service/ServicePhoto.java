package com.condocker.service;

import com.condocker.entity.Photo;
import com.condocker.repo.IPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ServicePhoto implements IServicePhoto{
    @Autowired
    private IPhoto photosDao;

//    @Autowired
//    private IMedianoDao medianoDao;

    @Override
    public List<Photo> getPhotos() {
        return photosDao.findAll();
    }

    @Override
    public List<Photo> getPhotosByMedianoId(String medianoId) {
        return photosDao.findByMedianoId(medianoId);
    }

//    @Override
//    public void addPhoto(PhotoDTO photo) {
//        Mediano mediano = medianoDao.
//                findById(photo.medianoId())
//                .orElseThrow
//                        (() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mediano no encontrado"));
//
//        Photo nuevaFoto = new Photo();
//        nuevaFoto.setPath(photo.url());
//        nuevaFoto.setDescription(photo.descripcion());
//        nuevaFoto.setMediano(mediano);
//
//        photosDao.save(nuevaFoto);
//    }
}
