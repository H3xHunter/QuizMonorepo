package com.condocker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PhotoDTO(
        Long id,
        @NotBlank
        String medianoId,
        @NotBlank
        String url,
        @NotBlank
        @Size(min = 5, max = 200)
        String descripcion) {

}
