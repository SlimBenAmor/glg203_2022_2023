package com.yaps.petstore;

/**
 * This exception is thrown when an object cannot be updated.
 */
public final class CustomerUpdateException extends CustomerException {

    public CustomerUpdateException(final String message) {
        super(message);
    }
}
