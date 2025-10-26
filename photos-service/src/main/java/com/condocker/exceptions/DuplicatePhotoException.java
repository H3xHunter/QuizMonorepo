package com.condocker.exceptions;

public class DuplicatePhotoException extends RuntimeException {
    public DuplicatePhotoException(String url) {
        super("Ya existe una foto con la URL '" + url + "'");
    }
}
