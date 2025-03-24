package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PlantPersistenceServiceTest {
    private PlantPersistenceService persistenceService;
    private PlantStorageManager plantStorageManager;

    @BeforeEach
    void setup() {
        plantStorageManager = mock(PlantStorageManager.class);
        persistenceService = new PlantPersistenceService(plantStorageManager);
    }

    @Test
    void persistPlant_ShouldReturnId() {
        CreatePlantRequest createPlantRequest = getRequest();
        String expectedPlantId = "dummy-tree-id-123";
        when(plantStorageManager.persist(createPlantRequest)).thenReturn(expectedPlantId);

        String actualPlantId = persistenceService.persist(createPlantRequest);
        verify(plantStorageManager).persist(createPlantRequest);
        assertEquals(expectedPlantId, actualPlantId, "Plant id mismatch");
    }

    private static CreatePlantRequest getRequest() {
        return CreatePlantRequest.builder()
                .name("Test Plant")
                .ageInDays(100)
                .lastWateredDate(new Date())
                .wateringIntervalInDays(20)
                .build();
    }
}
