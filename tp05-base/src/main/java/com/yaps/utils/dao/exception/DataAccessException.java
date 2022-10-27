package com.yaps.utils.dao.exception;

/**
 * This exception is throw when a general problem occurs in the persistent layer.
 */
public final class DataAccessException extends RuntimeException {

    public DataAccessException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public DataAccessException(String message) {
        super(message);
    }

    
}
