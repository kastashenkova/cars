package org.example.exception;

public class DuplicateCarException extends DomainException {

    public DuplicateCarException(String message) {
        super(message);
    }
}
