package com.condocker.controller;

import com.condocker.dto.MedianoDTO;
import com.condocker.dto.MedianoWithPhotosDTO;
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


    @GetMapping({"", "/"})
    public List<MedianoDTO> getMedianos(){
        return mediano.getMedianos();

    }
    @PostMapping({"", "/"})
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

    /**
     * Get mediano by ID
     * @param id The mediano's ID
     * @return Mediano entity
     */
    @GetMapping("/{id}")
    public Mediano getMedianoById(@PathVariable String id){
        return mediano.getMedianoById(id).orElse(null);
    }

    /**
     * Get mediano with all associated photos (inter-service communication via Feign)
     * @param id The mediano's ID
     * @return Mediano with list of photos
     */
    @GetMapping("/{id}/fotos")
    public MedianoWithPhotosDTO getMedianoWithPhotos(@PathVariable String id){
        return mediano.getMedianoWithPhotos(id);
    }

}
