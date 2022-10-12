package com.yaps.petstore;

public class CustomerCheckException extends CustomerException { 
    public CustomerCheckException(String errorMessage) {
        super(errorMessage);
    }
}