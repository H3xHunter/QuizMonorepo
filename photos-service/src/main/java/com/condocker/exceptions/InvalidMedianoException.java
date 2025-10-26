package com.condocker.exceptions;

public class InvalidMedianoException extends RuntimeException {
    public InvalidMedianoException(String medianoId) {
        super("El mediano con ID '" + medianoId + "' no existe o es inválido");
    }
}
