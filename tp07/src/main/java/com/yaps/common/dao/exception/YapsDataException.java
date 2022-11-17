package com.yaps.common.dao.exception;

import org.springframework.dao.DataAccessException;

/**
 * This exception is throw when a general problem occurs in the persistent layer.
 */
public final class YapsDataException extends DataAccessException {

    public YapsDataException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public YapsDataException(String message) {
        super(message);
    }

    
}
