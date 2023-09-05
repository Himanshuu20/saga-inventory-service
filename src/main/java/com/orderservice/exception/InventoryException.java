/**
 * @author  Himanshu Sagar
 * @version Java 17
 * @since   2023-08-30
 */

package com.orderservice.exception;

public class InventoryException extends Exception{
    public InventoryException(String exp){
        super(exp);
    }
}
