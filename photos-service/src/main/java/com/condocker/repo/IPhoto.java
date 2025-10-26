package com.condocker.repo;

import com.condocker.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPhoto extends JpaRepository<Photo,Long> {

    /**
     * Find all photos associated with a specific mediano
     * @param medianoId The mediano's ID
     * @return List of photos belonging to the mediano
     */
    List<Photo> findByMedianoId(String medianoId);
}