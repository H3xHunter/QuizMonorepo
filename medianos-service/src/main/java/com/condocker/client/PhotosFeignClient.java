package com.condocker.client;

import com.condocker.dto.PhotoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Feign client to communicate with Photos Service
 * Uses Eureka service discovery via service name "PHOTOS-SERVICE"
 */
@FeignClient(name = "PHOTOS-SERVICE")
public interface PhotosFeignClient {

    /**
     * Get all photos for a specific mediano
     * @param medianoId The mediano's ID
     * @return List of photos belonging to the mediano
     */
    @GetMapping("/api/fotos/mediano/{medianoId}")
    List<PhotoResponseDTO> getPhotosByMedianoId(@PathVariable("medianoId") String medianoId);
}
