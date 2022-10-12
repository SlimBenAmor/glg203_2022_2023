package com.yaps.petstore;

public class CustomerException extends Exception { 
    public CustomerException(String errorMessage) {
        super(errorMessage);
    }
}