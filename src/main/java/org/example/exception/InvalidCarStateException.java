package org.example.exception;

public class InvalidCarStateException extends DomainException {

    public InvalidCarStateException(String message) {
        super(message);
    }
}
