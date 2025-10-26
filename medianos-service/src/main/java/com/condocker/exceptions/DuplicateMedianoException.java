package com.condocker.exceptions;

public class DuplicateMedianoException extends RuntimeException {
    public DuplicateMedianoException(String name) {
        super("Ya existe un mediano con el nombre '" + name + "'");
    }
}
