package com.example.InventoryManager.unit_test;
import com.example.InventoryManager.model.Inventory;
import com.example.InventoryManager.model.OrderInfo;
import com.example.InventoryManager.producer.MessageProducer;
import com.example.InventoryManager.repo.InventoryRepo;
import com.example.InventoryManager.service.InventoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class InventoryServiceUnitTest {

    @InjectMocks
    InventoryService inventoryService;

    @Mock
    InventoryRepo inventoryRepo;

    @Mock
    MessageProducer messageProducer;

    @Test
    public void addItemUnitTest(){
        Inventory item = new Inventory();
        item.setItemId(1);
        item.setQty(15);
        item.setName("Lays");
        Mockito.when(inventoryRepo.save(item)).thenReturn(item);
        Assertions.assertEquals(item,this.inventoryService.addItem(item));
        Assertions.assertEquals(item.getItemId(),this.inventoryService.addItem(item).getItemId());
        Assertions.assertEquals(item.getQty(),this.inventoryService.addItem(item).getQty());
        Assertions.assertEquals(item.getName(),this.inventoryService.addItem(item).getName());
    }

    @Test
    public void getInventoryItemUnitTest(){
        Inventory item = new Inventory();
        item.setItemId(1);
        item.setQty(15);
        item.setName("Lays");
        Mockito.when(inventoryRepo.findById(item.getItemId())).thenReturn(java.util.Optional.of(item));
        Assertions.assertEquals(item,this.inventoryService.getItem(1));
    }

    @Test
    public void updateInventoryUnitTest(){
        Mockito.doNothing().when(inventoryRepo).updateInventory(1,5);
        Assertions.assertDoesNotThrow(()->inventoryService.updateInventory(1,5));
    }

    @Test
    public void getAllItemsUnitTest(){
        List<Inventory> list = new ArrayList<>();
        Inventory item = new Inventory();
        item.setItemId(1);
        item.setQty(15);
        item.setName("Lays");
        list.add(item);
        Mockito.when(inventoryRepo.findAll()).thenReturn(list);
        Assertions.assertEquals(list,inventoryService.getAllItems());
        Assertions.assertEquals(list.get(0),inventoryService.getAllItems().get(0));
        Assertions.assertEquals(list.get(0).getItemId(),inventoryService.getAllItems().get(0).getItemId());
    }

    @Test
    public void getItemUnitTest(){
        Inventory item = new Inventory();
        item.setItemId(1);
        item.setQty(15);
        item.setName("Lays");
        Mockito.when(inventoryRepo.findById(1)).thenReturn(java.util.Optional.of(item));
        Assertions.assertEquals(item,inventoryService.getItem(1));
        Assertions.assertEquals(item.getItemId(),inventoryService.getItem(1).getItemId());
        Assertions.assertEquals(item.getName(),inventoryService.getItem(1).getName());
    }

    @Test
    public void addQtyUnitTest(){
        Inventory item = new Inventory();
        item.setItemId(1);
        item.setQty(15);
        item.setName("Lays");
        Mockito.when(inventoryRepo.findById(1)).thenReturn(java.util.Optional.of(item));
        Mockito.when(inventoryRepo.save(item)).thenReturn(item);
        Assertions.assertDoesNotThrow(()->inventoryService.addQty(1,5));
    }

    @Test
    public void deleteItemUnitTest(){
        Mockito.doNothing().when(inventoryRepo).deleteById(1);
        Assertions.assertDoesNotThrow(()->inventoryService.deleteItem(1));
    }

    @Test
    public void updateItemQtyUnitTest(){
        Mockito.doNothing().when(messageProducer).sendMessage(new OrderInfo());
        Inventory item = new Inventory();
        item.setItemId(1);
        item.setQty(15);
        item.setName("Chicken Burger");
        Mockito.when(this.inventoryRepo.findById(1)).thenReturn(java.util.Optional.of(item));
        OrderInfo order = new OrderInfo();
        order.setOrderId(1);
        order.setOrderStatus("PLACED");
        order.setQty(1);
        order.setName("Chicken Burger");
        order.setPaymentStatus("ACCEPTED");
        order.setItemId(1);
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
