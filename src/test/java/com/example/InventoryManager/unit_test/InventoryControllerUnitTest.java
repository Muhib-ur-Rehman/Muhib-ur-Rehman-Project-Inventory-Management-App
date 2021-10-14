package com.example.InventoryManager.unit_test;
import com.example.InventoryManager.controller.InventoryController;
import com.example.InventoryManager.model.Inventory;
import com.example.InventoryManager.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class InventoryControllerUnitTest {

    @InjectMocks
    private InventoryController inventoryController;

    @Mock
    InventoryService inventoryService;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        mapper=new ObjectMapper();
    }

    @Test
    public void addItemUnitTest() throws Exception {
        Inventory item = new Inventory();
        item.setItemId(10);
        item.setQty(15);
        item.setName("Lays");
        Mockito.when(inventoryService.addItem(item)).thenReturn(item);
        System.out.println("Yai raha"+inventoryService.addItem(item));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/item").contentType("application/json").content(mapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(content().string("Item added successfully"));
    }

    @Test
    public void getAllItemsUnitTest() throws Exception {
        List<Inventory> list = new ArrayList<>();
        Inventory item = new Inventory();
        item.setItemId(10);
        item.setQty(15);
        item.setName("Lays");
        list.add(item);
        Mockito.when(inventoryService.getAllItems()).thenReturn(list);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/item").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\n" +
                        "    \"itemId\": 10,\n" +
                        "    \"name\": \"Lays\",\n" +
                        "    \"qty\": 15\n" +
                        "}]"));
    }

    @Test
    public void getItemUnitTest() throws Exception {
        Inventory item = new Inventory();
        item.setItemId(10);
        item.setQty(15);
        item.setName("Lays");
        Mockito.when(inventoryService.getItem(1)).thenReturn(item);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/item/1").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"itemId\": 10,\n" +
                        "    \"name\": \"Lays\",\n" +
                        "    \"qty\": 15\n" +
                        "}"));
    }

    @Test
    public void addQtyUnitTest() throws Exception {
        Mockito.doNothing().when(inventoryService).updateInventory(1,5);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/item/1/5").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item quantity is updated successfully"));
    }

    @Test
    public void deleteItemUnitTest() throws Exception {
        Mockito.doNothing().when(inventoryService).deleteItem(1);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/item/1").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item has deleted successfully!!"));
    }

}
