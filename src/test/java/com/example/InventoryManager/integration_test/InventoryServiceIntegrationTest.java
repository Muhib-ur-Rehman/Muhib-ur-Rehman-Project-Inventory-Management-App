package com.example.InventoryManager.integration_test;

import com.example.InventoryManager.model.Inventory;
import com.example.InventoryManager.model.OrderInfo;
import com.example.InventoryManager.producer.MessageProducer;
import com.example.InventoryManager.repo.InventoryRepo;
import com.example.InventoryManager.service.InventoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

@SpringBootTest
public class InventoryServiceIntegrationTest {

    @Autowired
    InventoryService inventoryService;

    @Mock
    MessageProducer messageProducer;

    @Mock
    InventoryRepo inventoryRepo;

    @MockBean
    RabbitTemplate template;



    @Test
    public void addItemIntegrationTest(){
        Inventory item = new Inventory();
        item.setItemId(11);
        item.setQty(15);
        item.setName("Lays");
        Inventory savedItem = this.inventoryService.addItem(item);
        Assertions.assertEquals(item.getItemId(),savedItem.getItemId());
        Assertions.assertEquals(item.getQty(),savedItem.getQty());
        Assertions.assertEquals(item.getName(),savedItem.getName());
    }

    @Test
    public void getInventoryItemIntegrationTest(){
        Inventory item = new Inventory();
        item.setItemId(11);
        item.setQty(15);
        item.setName("Lays");
        Assertions.assertEquals(item.getItemId(),this.inventoryService.getInventoryItem(11).get().getItemId());
        Assertions.assertEquals(item.getName(),this.inventoryService.getInventoryItem(11).get().getName());
        Assertions.assertEquals(item.getQty(),this.inventoryService.getInventoryItem(11).get().getQty());
    }

    @Test
    public void updateInventoryIntegrationTest(){
        Assertions.assertDoesNotThrow(()->this.inventoryService.updateInventory(11,15));
    }

    @Test
    public void getAllItemsIntegrationTest(){
        List<Inventory> list = this.inventoryService.getAllItems();
        Assertions.assertNotNull(list);
        Assertions.assertNotEquals(0,list.size());
    }

    @Test
    public void getItemIntegrationTest(){
        Inventory fetchedItem = this.inventoryService.getItem(11);
        Assertions.assertEquals(11,fetchedItem.getItemId());
        Assertions.assertEquals("Lays",fetchedItem.getName());
    }

    @Test
    public void addQtyIntegrationTest(){
        Assertions.assertDoesNotThrow(()->inventoryService.addQty(11,5));
    }

    @Test
    public void deleteItemIntegrationTest(){
        Assertions.assertDoesNotThrow(()->inventoryService.deleteItem(11));
    }

    @Test
    public void updateItemQtyIntegrationTest(){
        Mockito.doNothing().when(messageProducer).sendMessage(new OrderInfo());
        OrderInfo order = new OrderInfo();
        order.setOrderId(1);
        order.setOrderStatus("PLACED");
        order.setQty(1);
        order.setName("Chicken Burger");
        order.setPaymentStatus("ACCEPTED");
        order.setItemId(11);
        Assertions.assertEquals("IN-PROCESS",this.inventoryService.updateItemQty(order).getOrderStatus());
        Assertions.assertEquals("ACCEPTED",this.inventoryService.updateItemQty(order).getPaymentStatus());
        order.setQty(100);
        Assertions.assertEquals("CANCELLED",this.inventoryService.updateItemQty(order).getOrderStatus());
        Assertions.assertEquals("REFUNDED",this.inventoryService.updateItemQty(order).getPaymentStatus());
        order.setQty(1);
        order.setItemId(11111);
        Assertions.assertEquals("CANCELLED",this.inventoryService.updateItemQty(order).getOrderStatus());
        Assertions.assertEquals("REFUNDED",this.inventoryService.updateItemQty(order).getPaymentStatus());
    }
}
