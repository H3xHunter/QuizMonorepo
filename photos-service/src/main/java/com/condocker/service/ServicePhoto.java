package com.condocker.service;

import com.condocker.dto.PhotoDTO;
import com.condocker.entity.Photo;
import com.condocker.exceptions.DuplicatePhotoException;
import com.condocker.exceptions.PhotoNotFoundException;
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
        // Check for duplicate path/URL
        photosDao.findByPath(photo.url()).ifPresent(existing -> {
            throw new DuplicatePhotoException(photo.url());
        });

        Photo nuevaFoto = new Photo();
        nuevaFoto.setPath(photo.url());
        nuevaFoto.setDescription(photo.descripcion());
        nuevaFoto.setMedianoId(photo.medianoId());

        return photosDao.save(nuevaFoto);
    }

    @Override
    public Photo getPhotoById(Long id) {
        return photosDao.findById(id)
                .orElseThrow(() -> new PhotoNotFoundException(id));
    }

    @Override
    public Photo updatePhoto(Long id, PhotoDTO photoDTO) {
        Photo photo = photosDao.findById(id)
                .orElseThrow(() -> new PhotoNotFoundException(id));

        // Check if updating to a duplicate path (but not same photo)
        photosDao.findByPath(photoDTO.url()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicatePhotoException(photoDTO.url());
            }
        });

        photo.setPath(photoDTO.url());
        photo.setDescription(photoDTO.descripcion());
        photo.setMedianoId(photoDTO.medianoId());
        return photosDao.save(photo);
    }

    @Override
    public boolean deletePhoto(Long id) {
        if (!photosDao.existsById(id)) {
            throw new PhotoNotFoundException(id);
        }
        photosDao.deleteById(id);
        return true;
    }
}
