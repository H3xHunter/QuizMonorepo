package com.condocker.controller;

import com.condocker.dto.PhotoDTO;
import com.condocker.entity.Photo;
import com.condocker.service.IServicePhoto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fotos")
public class PhotoController {
    @Autowired
    private IServicePhoto servicePhoto;

    @GetMapping({"", "/"})
    public List<Photo> getAllPhotos() {
        return servicePhoto.getPhotos();
    }

    /**
     * Get all photos for a specific mediano
     * @param medianoId The mediano's ID
     * @return List of photos belonging to the mediano
     */
    @GetMapping("/mediano/{medianoId}")
    public List<Photo> getPhotosByMedianoId(@PathVariable("medianoId") String medianoId) {
        return servicePhoto.getPhotosByMedianoId(medianoId);
    }

    /**
     * Create a new photo
     * @param photoDTO The photo data
     * @return The created photo with ID
     */
    @PostMapping({"", "/"})
    public ResponseEntity<PhotoDTO> createPhoto(@RequestBody @Valid PhotoDTO photoDTO) {
        Photo created = servicePhoto.addPhoto(photoDTO);
        PhotoDTO response = new PhotoDTO(
                created.getId(),
                created.getMedianoId(),
                created.getPath(),
                created.getDescription()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Get photo by ID
     * @param id The photo's ID
     * @return The photo or 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<PhotoDTO> getPhotoById(@PathVariable("id") Long id) {
        Photo photo = servicePhoto.getPhotoById(id);
        if (photo == null) {
            return ResponseEntity.notFound().build();
        }
        PhotoDTO response = new PhotoDTO(
                photo.getId(),
                photo.getMedianoId(),
                photo.getPath(),
                photo.getDescription()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Update a photo
     * @param id The photo's ID
     * @param photoDTO Updated photo data
     * @return The updated photo or 404
     */
    @PutMapping("/{id}")
    public ResponseEntity<PhotoDTO> updatePhoto(@PathVariable("id") Long id, @RequestBody @Valid PhotoDTO photoDTO) {
        Photo updated = servicePhoto.updatePhoto(id, photoDTO);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        PhotoDTO response = new PhotoDTO(
                updated.getId(),
                updated.getMedianoId(),
                updated.getPath(),
                updated.getDescription()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a photo
     * @param id The photo's ID
     * @return 200 OK if deleted, 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePhoto(@PathVariable("id") Long id) {
        boolean deleted = servicePhoto.deletePhoto(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
