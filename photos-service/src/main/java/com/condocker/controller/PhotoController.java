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

    @GetMapping("/")
    public List<Photo> getPhoto(@PathVariable String idmediano) {
        return servicePhoto.getPhotos();

    }
    @PostMapping("/")
    public void createPhoto(@RequestBody PhotoDTO photo) {
        this.servicePhoto.addPhoto(photo);
    }

}
