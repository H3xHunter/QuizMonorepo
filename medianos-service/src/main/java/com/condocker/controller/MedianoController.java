package com.condocker.controller;

import com.condocker.dto.MedianoDTO;
import com.condocker.entity.Mediano;
import com.condocker.service.IMedianoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medianos")
public class MedianoController {
    @Autowired
    private IMedianoService mediano;


    @GetMapping("/")
    public List<MedianoDTO> getMedianos(){
        return mediano.getMedianos();

    }
    @PostMapping("/")
    public ResponseEntity<?> addMediano(@RequestBody @Valid
                                        MedianoDTO medianoDTO){
        System.out.println(medianoDTO);
        mediano.addMediano(medianoDTO);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/filtrado/{nombre}")
    public Mediano filtrado(@PathVariable String nombre){
        ///
        return mediano.getByName(nombre).orElse(null);
    }

//    @GetMapping("/fotos/{idMediano}")
//    public List<Photo> fotos(@PathVariable String idMediano){
//        return this.mediano.getPhotosMediano(idMediano);
//    }

}
