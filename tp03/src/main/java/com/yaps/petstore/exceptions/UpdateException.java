package com.yaps.petstore.exceptions;

/**
 * This exception is thrown when an object cannot be updated.
 */
public final class UpdateException extends YAPSException {

    public UpdateException(final String message) {
        super(message);
    }
}
