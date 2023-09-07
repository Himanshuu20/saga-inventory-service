package com.inventoryservice.exception;

public class OutOfStockException extends Exception{
    public OutOfStockException(String exp){
        super(exp);
    }
}
