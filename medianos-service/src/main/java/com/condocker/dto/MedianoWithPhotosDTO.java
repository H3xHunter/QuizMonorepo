package com.condocker.dto;

import java.util.List;

/**
 * DTO to return Mediano data along with associated photos
 */
public record MedianoWithPhotosDTO(
        String id,
        String nombre,
        long altura,
        String email,
        List<PhotoResponseDTO> photos
) {
}
