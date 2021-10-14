package com.example.InventoryManager.service;

import com.example.InventoryManager.model.Inventory;
import com.example.InventoryManager.repo.InventoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class InventoryService {

    @Autowired
    InventoryRepo inventoryRepo;

    public Inventory addItem(Inventory inventory){
        return this.inventoryRepo.save(inventory);
    }

    public Optional<Inventory> getInventoryItem(int itemId) {
        return this.inventoryRepo.findById(itemId);
    }

    public void updateInventory(int itemId, int newQty) {
        this.inventoryRepo.updateInventory(itemId,newQty);
    }

    public List<Inventory> getAllItems(){
        return this.inventoryRepo.findAll();
    }

    public Inventory getItem(int itemId){
        return this.inventoryRepo.findById(itemId).get();
    }

    public void addQty(int id , int qty){
        Inventory inventory = this.inventoryRepo.findById(id).get();
        inventory.setQty(inventory.getQty()+qty);
        this.inventoryRepo.save(inventory);
    }

    public void deleteItem(int itemId){
        this.inventoryRepo.deleteById(itemId);
    }
}
