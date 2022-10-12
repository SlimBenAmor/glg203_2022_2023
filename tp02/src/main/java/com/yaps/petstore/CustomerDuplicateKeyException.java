package com.yaps.petstore;

public class CustomerDuplicateKeyException extends CustomerCreateException { 
    public CustomerDuplicateKeyException(String errorMessage) {
        super(errorMessage);
    }
}