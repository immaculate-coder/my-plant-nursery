package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlantPersistanceServiceTest {

    @MockitoBean
    private PlantStorageManager plantStorageManager;

    @Autowired
    private PlantPersister plantPersister;

    @Test
    void persistPlant_ShouldReturnId() {
        CreatePlantRequest createPlantRequest = CreatePlantRequest.builder()
                .name("Test Plant")
                .ageInDays(100)
                .lastWateredDate(new Date())
                .wateringIntervalInDays(20)
                .build();
        String expectedPlantId = "dummy-tree-id-123";
        when(plantStorageManager.persist(any(CreatePlantRequest.class))).thenReturn(expectedPlantId);

        String actualPlantId = plantPersister.persist(createPlantRequest);
        assertEquals(expectedPlantId, actualPlantId, "Plant id mismatch");
        verify(plantStorageManager).persist(createPlantRequest);
    }
}
