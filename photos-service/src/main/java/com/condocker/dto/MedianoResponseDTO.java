package com.condocker.dto;

/**
 * DTO to receive Mediano data from Medianos Service
 */
public record MedianoResponseDTO(
        String id,
        String name,
        long height,
        String email
) {
}
