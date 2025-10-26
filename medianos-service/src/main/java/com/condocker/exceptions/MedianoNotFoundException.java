package com.condocker.exceptions;

public class MedianoNotFoundException extends RuntimeException {
    public MedianoNotFoundException(String id) {
        super("Mediano con ID '" + id + "' no encontrado");
    }
}
