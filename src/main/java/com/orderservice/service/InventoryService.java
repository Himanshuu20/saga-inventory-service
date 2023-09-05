/**
 * @author  Himanshu Sagar
 * @version Java 17
 * @since   2023-08-30
 */

package com.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.orderservice.dto.InventoryDto;
import com.orderservice.exception.InventoryException;
import com.orderservice.exception.LimitExceededException;
import com.orderservice.exception.OutOfStockException;

import java.util.List;

public interface InventoryService {
    InventoryDto getInventoryById(String inventoryId) throws InventoryException;
    List<InventoryDto> getInventories() throws InventoryException;
    String addItemInInventory(InventoryDto inventoryDto) throws InventoryException;
    String updateItemInInventory(InventoryDto inventoryDto) throws InventoryException;
    String deleteItemInInventoryById(String inventoryId) throws InventoryException;
    void consumeOrderEvent(String event) throws OutOfStockException, LimitExceededException, JsonProcessingException;
    void reverseInventory(String event) throws JsonProcessingException;
}
