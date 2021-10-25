package com.example.InventoryManager.producer;

import com.example.InventoryManager.config.InventoryConfig;
import com.example.InventoryManager.model.OrderInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendMessage(OrderInfo order){
        rabbitTemplate.convertAndSend(InventoryConfig.EXCHANGE,InventoryConfig.ROUTING_KEY,order);
    }
}
