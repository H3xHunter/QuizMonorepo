package com.condocker.service;


import com.condocker.dto.PhotoDTO;
import com.condocker.entity.Photo;

import java.util.List;

public interface IServicePhoto {
    public List<Photo> getPhotos();

    /**
     * Get all photos for a specific mediano
     * @param medianoId The mediano's ID
     * @return List of photos belonging to the mediano
     */
    public List<Photo> getPhotosByMedianoId(String medianoId);

}
