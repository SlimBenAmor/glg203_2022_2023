package com.yaps.petstore.exceptions;

/**
 * This exception is thrown when a Customer cannot be deleted.
 */
public final class RemoveException extends YAPSException {

    public RemoveException(final String message) {
        super(message);
    }
}
