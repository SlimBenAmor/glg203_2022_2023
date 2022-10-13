package com.yaps.petstore;

/**
 * This exception is thrown when a Customer cannot be deleted.
 */
public final class CustomerRemoveException extends CustomerException {

    public CustomerRemoveException(final String message) {
        super(message);
    }
}
