package com.example.InventoryManager.integration_test;

import com.example.InventoryManager.model.Inventory;
import com.example.InventoryManager.model.OrderInfo;
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
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class InventoryServiceIntegrationTest {

    @Autowired
    InventoryService inventoryService;

    @Mock
    InventoryRepo inventoryRepo;

    @MockBean
    RabbitTemplate template;

    @Test
    public void addItemIntegrationTest(){
        Inventory item = new Inventory();
        item.setItemId(10);
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
        item.setItemId(10);
        item.setQty(15);
        item.setName("Lays");
        Assertions.assertEquals(item.getItemId(),this.inventoryService.getInventoryItem(10).get().getItemId());
        Assertions.assertEquals(item.getName(),this.inventoryService.getInventoryItem(10).get().getName());
        Assertions.assertEquals(item.getQty(),this.inventoryService.getInventoryItem(10).get().getQty());
    }

    @Test
    public void updateInventoryIntegrationTest(){
        Assertions.assertDoesNotThrow(()->this.inventoryService.updateInventory(10,5));
    }

    @Test
    public void getAllItemsIntegrationTest(){
        List<Inventory> list = this.inventoryService.getAllItems();
        Assertions.assertNotNull(list);
        Assertions.assertNotEquals(0,list.size());
    }

    @Test
    public void getItemIntegrationTest(){
        Inventory fetchedItem = this.inventoryService.getItem(10);
        Assertions.assertEquals(10,fetchedItem.getItemId());
        Assertions.assertEquals("Lays",fetchedItem.getName());
    }

    @Test
    public void addQtyIntegrationTest(){
        Assertions.assertDoesNotThrow(()->inventoryService.addQty(10,5));
    }

    @Test
    public void deleteItemIntegrationTest(){
        Assertions.assertDoesNotThrow(()->inventoryService.deleteItem(10));
    }

    @Test
    public void consumeMessageFromQueueUnitTest(){
        Inventory item = new Inventory();
        item.setItemId(1);
        item.setQty(15);
        item.setName("Lays");
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setItemId(1);
        orderInfo.setOrderStatus("PLACED");
        orderInfo.setPaymentStatus("ACCEPTED");
        orderInfo.setQty(2);
        Mockito.when(inventoryRepo.findById(1)).thenReturn(java.util.Optional.of(item));
        Assertions.assertDoesNotThrow(()->this.inventoryService.consumeMessageFromQueue(orderInfo));
        orderInfo.setQty(200);
        Assertions.assertDoesNotThrow(()->this.inventoryService.consumeMessageFromQueue(orderInfo));
        orderInfo.setItemId(2222);
        Assertions.assertDoesNotThrow(()->this.inventoryService.consumeMessageFromQueue(orderInfo));
    }
}
