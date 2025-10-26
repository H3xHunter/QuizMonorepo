package com.condocker.controller;

import com.condocker.dto.PhotoDTO;
import com.condocker.entity.Photo;
import com.condocker.service.IServicePhoto;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Photo> getPhotosByMedianoId(@PathVariable String medianoId) {
        return servicePhoto.getPhotosByMedianoId(medianoId);
    }

    /*
    @PostMapping("/")
    public void createPhoto(@RequestBody PhotoDTO photo) {
        this.servicePhoto.addPhoto(photo);
    }
    */
}
