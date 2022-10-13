package com.yaps.petstore.exceptions;

/**
 * This exception is thrown when some checking validation error is found.
 */
public final class ValidationException extends YAPSException {

    public ValidationException(final String message) {
        super(message);
    }
}
