/**
 * @author  Himanshu Sagar
 * @version Java 17
 * @since   2023-08-30
 */

package com.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.dao.InventoryDao;
import com.inventoryservice.dto.InventoryDto;
import com.inventoryservice.entities.Inventory;
import com.inventoryservice.enums.Status;
import com.inventoryservice.events.Event;
import com.inventoryservice.events.OrderEvent;
import com.inventoryservice.exception.InventoryException;
import com.inventoryservice.exception.LimitExceededException;
import com.inventoryservice.exception.OutOfStockException;
import com.inventoryservice.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService{
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private MapperUtils mapperUtils;
    @Autowired
    private KafkaTemplate<String, Event> kafkaTemplate;

    @Override
    public InventoryDto getInventoryById(String inventoryId) throws InventoryException {
        log.info("fetch item by id");
        Optional<Inventory> item = inventoryDao.findById(inventoryId);
        if(item.isEmpty()){
            log.info("Error acquired in getInventoryById method");
            throw new InventoryException("No item present in the inventory of the given id : " + inventoryId);
        }
        InventoryDto inventoryDto = mapperUtils.convertToDto(item.get());
        log.info("item is fetched : " + inventoryDto);
        return inventoryDto;
    }

    @Override
    public List<InventoryDto> getInventories() throws InventoryException {
        log.info("Fetching the item in the Inventory");
        List<Inventory> inventoryList = inventoryDao.findAll();
        if(inventoryList.isEmpty()){
            log.info("Error acquired in getInventories method");
            throw new InventoryException("No item present in Inventory...");
        }
        List<InventoryDto> inventoryDtoList = new ArrayList<>();
        for(Inventory item: inventoryList){
            inventoryDtoList.add(mapperUtils.convertToDto(item));
        }
        log.info("Fetched all the items present in the inventory : " + inventoryDtoList);
        return inventoryDtoList;
    }

    @Override
    public String addItemInInventory(InventoryDto inventoryDto) throws InventoryException {
        log.info("Adding or update the item in the inventory {}", inventoryDto);
        Optional<Inventory> item = inventoryDao.getItemByName(inventoryDto.getItemName());
        if(item.isPresent()){
            log.info("item already present in db :" + item.get());
            item.get().setQuantity(item.get().getQuantity()+inventoryDto.getQuantity());
            Inventory updatedItem  = inventoryDao.save(item.get());
            return ("Item were already present in the Inventory, we updated the stock count : " +  updatedItem + " Thank You...");
        }
        inventoryDto.setInventoryId(UUID.randomUUID().toString());
        Inventory itemStoredInDb = inventoryDao.save(mapperUtils.convertToDao(inventoryDto));
        log.info("Added or update the item in the inventory {} ", itemStoredInDb);
        return (itemStoredInDb + " Added to DB");
    }

    @Override
    public String updateItemInInventory(InventoryDto inventoryDto) throws InventoryException {
        log.info("calling the add method from update method");
        return addItemInInventory(inventoryDto);
    }

    @Override
    public String deleteItemInInventoryById(String inventoryId) throws InventoryException {
        log.info("delete the item from inventory");
        if(!inventoryDao.existsById(inventoryId)){
            log.info("Error acquired in deleteItemInInventoryById method");
            throw new InventoryException("Item is not present in the Database.");
        }
        inventoryDao.deleteById(inventoryId);
        log.info("Item is deleted from the inventory for the given id {} ", inventoryId);
        return "Item removed from DB";
    }
    @Override
    @KafkaListener(topics = "new-orders", groupId = "orders-group")
    public void consumeOrderEvent(String event) throws OutOfStockException, LimitExceededException, JsonProcessingException {
        log.info("consume data of order event from kafka {}", event);
        Event inventoryEvent = new Event();
        Event consumeOrderEventObject = new ObjectMapper().readValue(event, Event.class);
        Optional<Inventory> item = inventoryDao.getItemByName(consumeOrderEventObject.getOrderEvent().getName());

        if(item.isEmpty()){
            log.info("Item is not present in the inventory");
            inventoryEvent.setStatus(Status.OUT_OF_STOCK);
            inventoryEvent.setOrderEvent(consumeOrderEventObject.getOrderEvent());
            kafkaTemplate.send("reversed-orders",inventoryEvent);
            throw new OutOfStockException("Item is not present in the inventory.");
        }

        if(consumeOrderEventObject.getOrderEvent().getQuantity() > item.get().getQuantity()){
            log.info("Full quantity is not available in the inventory, number of unit present are {} ", item.get().getQuantity());
            inventoryEvent.setStatus(Status.LIMIT_EXCEEDED);
            inventoryEvent.setOrderEvent(consumeOrderEventObject.getOrderEvent());
            kafkaTemplate.send("reversed-orders",inventoryEvent);
            throw new LimitExceededException("Full quantity is not available in the inventory, number of unit present are : " + item.get().getQuantity());
        }

        item.get().setQuantity(item.get().getQuantity() - consumeOrderEventObject.getOrderEvent().getQuantity());
        Inventory itemStockUpdated = inventoryDao.save(item.get());
        log.info("updating the stock count {}",itemStockUpdated);

        inventoryEvent.setStatus(Status.INVENTORY_UPDATED);
        inventoryEvent.setOrderEvent(consumeOrderEventObject.getOrderEvent());
        kafkaTemplate.send("new-payments",inventoryEvent);
        log.info("produce inventory event data to the payment service");
    }

    @Override
    @KafkaListener(topics = "reversed-inventory", groupId = "inventory-group")
    public void reverseInventory(String event) throws JsonProcessingException {
        log.info("Inventory reversed  {}",event);
        Event inventoryEvent = new ObjectMapper().readValue(event, Event.class);
        Inventory item = inventoryDao.getItemByName(inventoryEvent.getOrderEvent().getName()).get();
        log.info("item fetched from db {}", item);
        item.setQuantity(item.getQuantity() + inventoryEvent.getOrderEvent().getQuantity());
        inventoryDao.save(item);
//        inventoryEvent.setStatus(InventoryStatus.PAYMENT_FAILED);
        kafkaTemplate.send("reversed-orders", inventoryEvent);
        log.info("produced data for order service");
    }
}
