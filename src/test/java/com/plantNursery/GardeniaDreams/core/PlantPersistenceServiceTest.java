package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.exceptions.PlantNotFoundException;
import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import com.plantNursery.GardeniaDreams.core.model.UpdatePlantRequest;
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

    @Test
    void updatePlant_shouldReturnUpdatedPlant() {
        String plantId = "test-id-2";
        UpdatePlantRequest updatePlantRequest = getUpdatePlantRequest();

        Plant expectedPlant = getTestPlant();
        when(plantStorageManager.updatePlant(anyString(), any(UpdatePlantRequest.class))).thenReturn(expectedPlant);

        Plant actualPlant = persistenceService.updatePlant(plantId, updatePlantRequest);

        verify(plantStorageManager).updatePlant(plantId, updatePlantRequest);
        assertEquals(expectedPlant, actualPlant, "Updated plant does not match expected");
    }

    @Test
    void updatePlant_shouldThrowPlantNotFoundException() {
        String plantId = "non-existent-id";
        UpdatePlantRequest updatePlantRequest = UpdatePlantRequest.builder().build();

        doThrow(new PlantNotFoundException("Plant not found with id : " + plantId))
                .when(plantStorageManager).updatePlant(anyString(), any(UpdatePlantRequest.class));

        PlantNotFoundException exception = assertThrows(
                PlantNotFoundException.class,
                () -> persistenceService.updatePlant(plantId, updatePlantRequest),
                "Expected updatePlant() to throw PlantNotFoundException"
        );

        assertEquals("Plant not found with id : " + plantId, exception.getMessage());
        verify(plantStorageManager, times(1)).updatePlant(plantId, updatePlantRequest);
    }

    private static UpdatePlantRequest getUpdatePlantRequest() {
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        String plantName = "Test Updated Plant";
        int ageInDays = 200;
        int wateringIntervalInDays = 10;

        return UpdatePlantRequest.builder()
                .name(plantName)
                .ageInDays(ageInDays)
                .lastWateredDate(yesterday)
                .wateringIntervalInDays(wateringIntervalInDays)
                .build();
    }

    private static Plant getTestPlant() {
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        return Plant.builder()
                .name("Test Updated Plant")
                .ageInDays(200)
                .wateringIntervalInDays(10)
                .lastWateredDate(yesterday)
                .build();
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
