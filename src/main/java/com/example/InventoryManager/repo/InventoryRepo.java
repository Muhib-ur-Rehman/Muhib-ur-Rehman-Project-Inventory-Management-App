package com.example.InventoryManager.repo;

import com.example.InventoryManager.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface InventoryRepo extends JpaRepository<Inventory,Integer> {

    @Modifying
    @Transactional
    @Query(value = "update inventory set qty = :newQty where item_id = :itemId" , nativeQuery = true)
    public void updateInventory(@Param("itemId") int itemId,@Param("newQty") int newQty);
}
