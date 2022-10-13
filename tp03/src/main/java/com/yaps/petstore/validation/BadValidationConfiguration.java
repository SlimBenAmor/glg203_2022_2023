package com.yaps.petstore.validation;

/**
 * An exception used to signal bad uses of validation annotations.
 * They are bug, and as such should not be catched, but fixed in the code.
 */
public class BadValidationConfiguration extends RuntimeException {

    public BadValidationConfiguration(String message) {
        super(message);
    }

}
