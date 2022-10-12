package com.yaps.petstore;

public class CustomerNotFoundException extends CustomerFinderException { 
    public CustomerNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}