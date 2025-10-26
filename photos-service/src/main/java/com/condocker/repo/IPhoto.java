package com.condocker.repo;

import com.condocker.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IPhoto extends JpaRepository<Photo,Long> {

    /**
     * Find all photos associated with a specific mediano
     * @param medianoId The mediano's ID
     * @return List of photos belonging to the mediano
     */
    List<Photo> findByMedianoId(String medianoId);

    /**
     * Find photo by path (URL)
     * @param path The photo's path/URL
     * @return Optional containing the photo if found
     */
    Optional<Photo> findByPath(String path);
}