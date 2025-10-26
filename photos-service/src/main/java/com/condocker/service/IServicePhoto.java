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

    /**
     * Add a new photo
     * @param photo The photo DTO
     * @return The created photo entity
     */
    public Photo addPhoto(PhotoDTO photo);

    /**
     * Get photo by ID
     * @param id The photo's ID
     * @return Photo entity or null
     */
    public Photo getPhotoById(Long id);

    /**
     * Update a photo
     * @param id The photo's ID
     * @param photoDTO Updated photo data
     * @return Updated photo entity or null if not found
     */
    public Photo updatePhoto(Long id, PhotoDTO photoDTO);

    /**
     * Delete a photo
     * @param id The photo's ID
     * @return True if deleted, false if not found
     */
    public boolean deletePhoto(Long id);

}
