package com.yaps.petstore.exceptions;

/**
 * This abstract exception is the superclass of all application exception.
 * It is a checked exception because it extends the Exception class.
 */
public abstract class YAPSException extends Exception {

    protected YAPSException() {
    }

    protected YAPSException(final String message) {
        super(message);
    }
}
