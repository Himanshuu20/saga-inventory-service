/**
 * @author  Himanshu Sagar
 * @version Java 17
 * @since   2023-08-30
 */

package com.inventoryservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    @Id
    private String inventoryId;
    private String itemName;
    private Integer quantity;
}
