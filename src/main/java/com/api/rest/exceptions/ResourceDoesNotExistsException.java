package com.api.rest.exceptions;

public class ResourceDoesNotExistsException extends RuntimeException {
    public ResourceDoesNotExistsException(String message) {
        super(message);
    }
}
