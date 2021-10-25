package com.example.InventoryManager.integration_test;

import com.example.InventoryManager.controller.InventoryController;
import com.example.InventoryManager.model.Inventory;
import com.example.InventoryManager.repo.InventoryRepo;
import com.example.InventoryManager.service.InventoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.SmartDataSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class InventoryControllerIntegrationTest {

    @Autowired
    InventoryController inventoryController;
    @Autowired
    static DataSource dataSource;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        mapper=new ObjectMapper();
    }

    @AfterAll
    public static void closeDBConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.close();
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
}
