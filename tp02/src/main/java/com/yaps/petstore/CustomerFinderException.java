package com.yaps.petstore;

public class CustomerFinderException extends CustomerException { 
    public CustomerFinderException(String errorMessage) {
        super(errorMessage);
    }
}