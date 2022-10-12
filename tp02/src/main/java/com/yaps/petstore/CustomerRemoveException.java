package com.yaps.petstore;

public class CustomerRemoveException extends CustomerException { 
    public CustomerRemoveException(String errorMessage) {
        super(errorMessage);
    }
}