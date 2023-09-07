/**
 * @author  Himanshu Sagar
 * @version Java 17
 * @since   2023-08-30
 */

package com.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inventoryservice.dto.InventoryDto;
import com.inventoryservice.exception.InventoryException;
import com.inventoryservice.exception.LimitExceededException;
import com.inventoryservice.exception.OutOfStockException;

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
