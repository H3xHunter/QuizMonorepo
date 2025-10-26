package com.condocker.client;

import com.condocker.dto.MedianoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client to communicate with Medianos Service
 * Uses Eureka service discovery via service name "MEDIANOS-SERVICE"
 */
@FeignClient(name = "MEDIANOS-SERVICE")
public interface MedianosFeignClient {

    /**
     * Get mediano details by ID
     * @param id The mediano's ID
     * @return Mediano information
     */
    @GetMapping("/api/medianos/{id}")
    MedianoResponseDTO getMedianoById(@PathVariable("id") String id);
}
