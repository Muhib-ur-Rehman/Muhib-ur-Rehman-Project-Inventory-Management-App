package com.example.InventoryManager.unit_test;

import com.example.InventoryManager.repo.InventoryRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class InventoryRepoUnitTest {

    @Mock
    InventoryRepo inventoryRepo;

    @Test
    public void updateInventoryUnitTest(){
        Assertions.assertDoesNotThrow(()->inventoryRepo.updateInventory(1,5));
    }
}
