package com.yaps.petstore;

/**
 * This exception is thrown when some checking validation error is found.
 */
public final class CustomerCheckException extends CustomerException {

    public CustomerCheckException(final String message) {
        super(message);
    }
}
