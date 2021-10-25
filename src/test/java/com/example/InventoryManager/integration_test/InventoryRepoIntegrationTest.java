package com.example.InventoryManager.integration_test;

import com.example.InventoryManager.repo.InventoryRepo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
public class InventoryRepoIntegrationTest {

    @Autowired
    InventoryRepo inventoryRepo;

    @Autowired
    static DataSource dataSource;

    @AfterAll
    public static void closeDBConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.close();
    }

    @Test
    @Transactional
    public void updateInventoryIntegrationTest(){
        Assertions.assertDoesNotThrow(()->inventoryRepo.updateInventory(10,5));
    }
}
