package com.yaps.petstore;

/**
 * This abstract exception is the superclass of all application exception.
 * It is a checked exception because it extends the Exception class.
 */
public abstract class CustomerException extends Exception {

    protected CustomerException() {
    }

    protected CustomerException(final String message) {
        super(message);
    }
}
