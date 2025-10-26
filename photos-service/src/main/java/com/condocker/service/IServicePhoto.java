package com.condocker.service;


import com.condocker.dto.PhotoDTO;
import com.condocker.entity.Photo;

import java.util.List;

public interface IServicePhoto {
    public List<Photo> getPhotos();

}
