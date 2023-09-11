package com.inventoryservice.events;

import com.inventoryservice.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private Status status;
    private OrderEvent orderEvent;
}
