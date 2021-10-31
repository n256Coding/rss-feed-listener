package com.n256.rssfeedlistener.exception;

/**
 * A custom exception to handle errors related to validation of input fields of REST APIs.
 */
public class ValidationFailedException extends RuntimeException {

    public ValidationFailedException(String message) {
        super(message);
    }
}
