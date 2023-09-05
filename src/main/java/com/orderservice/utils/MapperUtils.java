/**
 * @author  Himanshu Sagar
 * @version Java 17
 * @since   2023-08-30
 */

package com.orderservice.utils;

import com.orderservice.dto.InventoryDto;
import com.orderservice.entities.Inventory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperUtils {
    @Autowired
    private ModelMapper modelMapper;

    public Inventory convertToDao(InventoryDto inventoryDto){
        return modelMapper.map(inventoryDto,Inventory.class);
    }

    public InventoryDto convertToDto(Inventory inventory){
        return modelMapper.map(inventory,InventoryDto.class);
    }
}
