/**
 * @author  Himanshu Sagar
 * @version Java 17
 * @since   2023-08-30
 */

package com.inventoryservice.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExceptionResponse {
    private String message;
    private LocalDateTime dateTime;
}
