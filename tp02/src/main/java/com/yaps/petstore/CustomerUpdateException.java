package com.yaps.petstore;

public class CustomerUpdateException extends CustomerException { 
    public CustomerUpdateException(String errorMessage) {
        super(errorMessage);
    }
}