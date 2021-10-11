package com.example.InventoryManager.controller;

import com.example.InventoryManager.model.Inventory;
import com.example.InventoryManager.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@RestController
public class inventoryController {

    @Autowired
    InventoryService inventoryService;

    @PostMapping("/item")
    public String addItem(@RequestBody Inventory inventory){
        this.inventoryService.addItem(inventory);
        return "Item added successfully";
    }

    @GetMapping("/item")
    public List<Inventory> getAllItems(){
        return this.inventoryService.getAllItems();
    }

    @GetMapping("/item/{id}")
    public Inventory getItem(@PathVariable("id") int itemId){
        return this.inventoryService.getItem(itemId).get();
    }

    @PutMapping("item/{id}/{qty}")
    public String addQty(@PathVariable("id") int itemId , @PathVariable("qty") int qty){
        this.inventoryService.addQty(itemId,qty);
        return "Item quantity is updated successfully";
    }

    @DeleteMapping("/item/{id}")
    public String deleteItem(@PathVariable("id") int itemId){
        this.inventoryService.deleteItem(itemId);
        return "Item has deleted successfully!!";
    }
}
