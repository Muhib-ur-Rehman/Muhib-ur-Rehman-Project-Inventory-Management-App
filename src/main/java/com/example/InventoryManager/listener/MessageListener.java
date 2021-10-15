package com.example.InventoryManager.listener;
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
public class MessageListener {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    InventoryService inventoryService;


    @RabbitListener(queues = InventoryConfig.INVENTORY_QUEUE)
    public void consumeMessageFromQueue(OrderInfo order){
        System.out.println("Message received from queue : " + order);
        this.inventoryService.updateItemQty(order);
    }
}
