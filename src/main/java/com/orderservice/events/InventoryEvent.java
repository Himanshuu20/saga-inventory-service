package com.orderservice.events;

import com.orderservice.enums.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryEvent {
    private InventoryStatus status;
    private OrderEvent orderEvent;
}
