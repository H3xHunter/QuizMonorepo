package com.condocker.repo;

import com.condocker.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPhoto extends JpaRepository<Photo,Long> {

    List<Photo> findByMedianoId(String medianoId);
}