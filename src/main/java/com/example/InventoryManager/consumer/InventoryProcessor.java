package com.example.InventoryManager.consumer;

import com.example.InventoryManager.config.InventoryConfig;
import com.example.InventoryManager.model.Inventory;
import com.example.InventoryManager.model.OrderInfo;
import com.example.InventoryManager.service.InventoryService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class InventoryProcessor {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    InventoryService inventoryService;

    @RabbitListener(queues = InventoryConfig.INVENTORY_QUEUE)
    public void consumeMessageFromQueue(OrderInfo order){
        System.out.println("Message received from queue : " + order);
        try {
            if (order.getPaymentStatus().equals("ACCEPTED")){
                Inventory orderedItemInfo = this.inventoryService.getInventoryItem(order.getItemId()).get();
                if (order.getQty() > orderedItemInfo.getQty()){
                    order.setOrderStatus("CANCELLED");
                    template.convertAndSend(InventoryConfig.EXCHANGE,InventoryConfig.ROUTING_KEY,order);
                }
                else if (order.getQty() <= orderedItemInfo.getQty()){
                    order.setOrderStatus("IN-PROCESS");
                    template.convertAndSend(InventoryConfig.EXCHANGE,InventoryConfig.ROUTING_KEY,order);
                    this.inventoryService.updateInventory(order.getItemId() ,orderedItemInfo.getQty() - order.getQty());
                }
            }
        }
        catch (NoSuchElementException e){
            order.setOrderStatus("CANCELLED");
            template.convertAndSend(InventoryConfig.EXCHANGE,InventoryConfig.ROUTING_KEY,order);
        }
    }
}
