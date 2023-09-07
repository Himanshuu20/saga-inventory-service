package com.inventoryservice.exception;

public class LimitExceededException extends Exception{
    public LimitExceededException(String exp){
        super(exp);
    }
}
