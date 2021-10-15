package com.example.InventoryManager.service;

import com.example.InventoryManager.config.InventoryConfig;
import com.example.InventoryManager.model.Inventory;
import com.example.InventoryManager.model.OrderInfo;
import com.example.InventoryManager.repo.InventoryRepo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@Transactional
public class InventoryService {

    @Autowired
    InventoryRepo inventoryRepo;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    InventoryService inventoryService;

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

    @RabbitListener(queues = InventoryConfig.INVENTORY_QUEUE)
    public void consumeMessageFromQueue(OrderInfo order){
        System.out.println("Message received from queue : " + order);
        try {
            if (order.getPaymentStatus().equals("ACCEPTED")){
                Inventory orderedItemInfo = inventoryRepo.findById(order.getItemId()).get();
                if (order.getQty() > orderedItemInfo.getQty()){
                    order.setOrderStatus("CANCELLED");
                    template.convertAndSend(InventoryConfig.EXCHANGE,InventoryConfig.ROUTING_KEY,order);
                }
                else if (order.getQty() <= orderedItemInfo.getQty()){
                    order.setOrderStatus("IN-PROCESS");
                    template.convertAndSend(InventoryConfig.EXCHANGE,InventoryConfig.ROUTING_KEY,order);
                    updateInventory(order.getItemId() ,orderedItemInfo.getQty() - order.getQty());
                }
            }
        }
        catch (NoSuchElementException e){
            order.setPaymentStatus("REFUNDED");
            order.setOrderStatus("CANCELLED");
            template.convertAndSend(InventoryConfig.EXCHANGE,InventoryConfig.ROUTING_KEY,order);
        }
    }
}
