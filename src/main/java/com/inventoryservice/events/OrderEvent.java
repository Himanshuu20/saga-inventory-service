package com.inventoryservice.events;

import com.inventoryservice.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private String id;
    private String name;
    private int quantity;
    private double amount;
    private Status status;
}
