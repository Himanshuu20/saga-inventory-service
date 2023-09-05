/**
 * @author  Himanshu Sagar
 * @version Java 17
 * @since   2023-08-30
 */

package com.orderservice.controller;

import com.orderservice.dto.InventoryDto;
import com.orderservice.exception.InventoryException;
import com.orderservice.service.InventoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    @Autowired
    private InventoryServiceImpl inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryDto>> getInventoriesItems() throws InventoryException {
        return new ResponseEntity<>(inventoryService.getInventories(),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<InventoryDto> getInventoryItemById(@PathVariable String id) throws InventoryException {
        return new ResponseEntity<>(inventoryService.getInventoryById(id), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<String> addItemInInventory(@RequestBody InventoryDto inventoryDto) throws InventoryException{
        return new ResponseEntity<>(inventoryService.addItemInInventory(inventoryDto),HttpStatus.CREATED);
    }
    @PutMapping
    public ResponseEntity<String> updateItemInInventory(@RequestBody InventoryDto inventoryDto) throws InventoryException{
        return new ResponseEntity<>(inventoryService.updateItemInInventory(inventoryDto),HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItemInInventory(@PathVariable String id) throws InventoryException{
        return new ResponseEntity<>(inventoryService.deleteItemInInventoryById(id),HttpStatus.OK);
    }
}
