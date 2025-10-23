package com.condocker.repo;

import com.condocker.entity.Mediano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IMediano extends JpaRepository<Mediano,String> {
    //SELECT * FROM MEDIANO WHERE NOMBRE=NOMBRE
    Optional<Mediano> findMedianoByName(String nombre);
}
