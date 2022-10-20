package com.yaps.petstore.exceptions;

/**
 * This exception is throw when a Customer already exists in the hashmap
 * and we want to add another one with the same identifier.
 */
public final class DuplicateKeyException extends CreateException {
}
