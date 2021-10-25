package com.example.InventoryManager.integration_test;

import com.example.InventoryManager.controller.InventoryController;
import com.example.InventoryManager.model.Inventory;
import com.example.InventoryManager.model.OrderInfo;
import com.example.InventoryManager.producer.MessageProducer;
import com.example.InventoryManager.repo.InventoryRepo;
import com.example.InventoryManager.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class InventoryControllerIntegrationTest {

    @Autowired
    InventoryController inventoryController;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        mapper=new ObjectMapper();
    }

    @Test
    @Order(1)
    public void addItemIntegrationTest() throws Exception {
        Inventory item = new Inventory();
        item.setItemId(10);
        item.setQty(15);
        item.setName("Lays");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/item").contentType("application/json").content(mapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(content().string("Item added successfully"));
    }

    @Test
    @Order(2)
    public void getAllItemsIntegrationTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/item").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"itemId\":10,\"name\":\"Lays\",\"qty\":15}")));
    }

    @Test
    @Order(3)
    public void getItemIntegrationTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/item/10").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"itemId\": 10,\n" +
                        "    \"name\": \"Lays\",\n" +
                        "    \"qty\": 15\n" +
                        "}"));
    }

    @Test
    @Order(4)
    public void addQtyIntegrationTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/item/10/5").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item quantity is updated successfully"));
    }

    @Test
    @Order(5)
    public void deleteItemIntegrationTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/item/10").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item has deleted successfully!!"));
    }

    @Autowired
    InventoryService inventoryService;


    @Mock
    MessageProducer messageProducer;

    @Mock
    InventoryRepo inventoryRepo;

    @MockBean
    RabbitTemplate template;



    @Test
    public void addItemIntegrationTest2(){
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
    public void getAllItemsIntegrationTest2(){
        List<Inventory> list = this.inventoryService.getAllItems();
        Assertions.assertNotNull(list);
        Assertions.assertNotEquals(0,list.size());
    }

    @Test
    public void getItemIntegrationTest2(){
        Inventory fetchedItem = this.inventoryService.getItem(11);
        Assertions.assertEquals(11,fetchedItem.getItemId());
        Assertions.assertEquals("Lays",fetchedItem.getName());
    }

//    @Test
//    public void addQtyIntegrationTest2(){
//        Inventory item = new Inventory();
//        item.setItemId(11);
//        item.setQty(15);
//        item.setName("Lays");
//        this.inventoryService.addItem(item);
//        Assertions.assertDoesNotThrow(()->inventoryService.addQty(11,5));
//    }

    @Test
    public void deleteItemIntegrationTest2(){
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


    @Test
    @Transactional
    public void updateInventoryIntegrationTest2(){
        Assertions.assertDoesNotThrow(()->inventoryRepo.updateInventory(10,5));
    }
}
