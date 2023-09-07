/**
 * @author  Himanshu Sagar
 * @version Java 17
 * @since   2023-08-30
 */

package com.inventoryservice.dao;

import com.inventoryservice.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryDao extends JpaRepository<Inventory,String> {

    @Query(value = "select * from inventory i where i.item_name=:name",nativeQuery = true)
    Optional<Inventory> getItemByName(@Param("name") String name);
}
