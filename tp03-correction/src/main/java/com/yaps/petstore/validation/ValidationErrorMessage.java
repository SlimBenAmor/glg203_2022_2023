package com.yaps.petstore.validation;

/**
 * An error message for a validation check. 
 * Typically returned as Optional by MethodChecker.
 * This class could probably be replaced by a simple String,
 * but its use will make the validator class more readable.
 */
public class ValidationErrorMessage {
    private String message;

    public ValidationErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
