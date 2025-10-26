package com.condocker.service;

import com.condocker.dto.PhotoDTO;
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

    @Override
    public Photo addPhoto(PhotoDTO photo) {
        Photo nuevaFoto = new Photo();
        nuevaFoto.setPath(photo.url());
        nuevaFoto.setDescription(photo.descripcion());
        nuevaFoto.setMedianoId(photo.medianoId());

        return photosDao.save(nuevaFoto);
    }

    @Override
    public Photo getPhotoById(Long id) {
        return photosDao.findById(id).orElse(null);
    }

    @Override
    public Photo updatePhoto(Long id, PhotoDTO photoDTO) {
        return photosDao.findById(id).map(photo -> {
            photo.setPath(photoDTO.url());
            photo.setDescription(photoDTO.descripcion());
            photo.setMedianoId(photoDTO.medianoId());
            return photosDao.save(photo);
        }).orElse(null);
    }

    @Override
    public boolean deletePhoto(Long id) {
        if (photosDao.existsById(id)) {
            photosDao.deleteById(id);
            return true;
        }
        return false;
    }
}
