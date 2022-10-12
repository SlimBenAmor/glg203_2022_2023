package com.yaps.petstore;

public class CustomerCreateException extends CustomerException { 
    public CustomerCreateException(String errorMessage) {
        super(errorMessage);
    }
}