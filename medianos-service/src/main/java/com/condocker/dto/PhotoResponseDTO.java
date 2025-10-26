package com.condocker.dto;

import java.time.LocalDate;

/**
 * DTO to receive Photo data from Photos Service
 */
public record PhotoResponseDTO(
        Long id,
        String path,
        String description,
        LocalDate created,
        String medianoId
) {
}
