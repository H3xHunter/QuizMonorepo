package com.condocker.exceptions;

public class PhotoNotFoundException extends RuntimeException {
    public PhotoNotFoundException(Long id) {
        super("Foto con ID '" + id + "' no encontrada");
    }
}
