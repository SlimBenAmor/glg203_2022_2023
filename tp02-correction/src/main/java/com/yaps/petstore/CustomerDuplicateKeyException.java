package com.yaps.petstore;

/**
 * This exception is throw when a Customer already exists in the hashmap
 * and we want to add another one with the same identifier.
 */
public final class CustomerDuplicateKeyException extends CustomerCreateException {
}
