package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.exceptions.PlantNotFoundException;
import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void deletePlant_shouldReturnPlantId() {
        String plantId = "test-id-1";
        when(plantStorageManager.deletePlant(plantId)).thenReturn(plantId);

        String actualPlantId = persistenceService.deletePlant(plantId);
        verify(plantStorageManager).deletePlant(plantId);
        assertEquals(plantId, actualPlantId, "Plant id mismatch");

    }

    @Test
    void deletePlant_shouldThrowPlantNotFoundException() {
        String plantId = "non-existent-id";
        doThrow(new PlantNotFoundException("Plant not found with id : " + plantId))
                .when(plantStorageManager).deletePlant(plantId);

        PlantNotFoundException exception = assertThrows(
                PlantNotFoundException.class,
                () -> persistenceService.deletePlant(plantId),
                "Expected deletePlant() to throw PlantNotFoundException"
        );

        assertEquals("Plant not found with id : " + plantId, exception.getMessage());
        verify(plantStorageManager, times(1)).deletePlant(plantId);
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
