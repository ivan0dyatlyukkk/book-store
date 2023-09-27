package org.diatliuk.bookstore.exception;

public class IllegalUserAccessException extends RuntimeException {
    public IllegalUserAccessException(String message) {
        super(message);
    }
}
