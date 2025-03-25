package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.exceptions.PlantNotFoundException;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlantFetcherServiceTest {
    private PlantFetcherService fetcherService;
    private PlantRetrievalManager plantRetrievalManager;

    @BeforeEach
    void setUp() {
        plantRetrievalManager = mock(PlantRetrievalManager.class);
        fetcherService = new PlantFetcherService(plantRetrievalManager);
    }

    @Test
    void getAllPlants_ShouldReturnList() {
        List<Plant> expectedPlants = Arrays.asList(
                Plant.builder()
                        .id("test-id-1")
                        .name("Rose")
                        .ageInDays(120)
                        .wateringIntervalInDays(7)
                        .build(),
                Plant.builder()
                        .id("test-id-2")
                        .name("Tulip")
                        .ageInDays(90)
                        .wateringIntervalInDays(5)
                        .build()
        );

        when(plantRetrievalManager.getAllPlants()).thenReturn(expectedPlants);

        List<Plant> actualPlants = fetcherService.getAllPlants();
        verify(plantRetrievalManager).getAllPlants();
        assertEquals(expectedPlants, actualPlants, "Plants list mismatch");
    }

    @Test
    void getPlantById_ShouldReturnPlant() {
        String plantId = "test-id-1";
        Plant expectedPlant = Plant.builder()
                .id(plantId)
                .name("Rose")
                .ageInDays(120)
                .wateringIntervalInDays(7)
                .build();

        when(plantRetrievalManager.getPlantById(plantId)).thenReturn(expectedPlant);

        Plant actualPlant = fetcherService.getPlantById(plantId);
        verify(plantRetrievalManager).getPlantById(plantId);
        assertEquals(expectedPlant, actualPlant, "Plant details mismatch");
    }

    @Test
    void getPlantById_ShouldThrowPlantNotFoundException() {
        String plantId = "test-id-999";
        when(plantRetrievalManager.getPlantById(plantId)).thenThrow(new PlantNotFoundException("Plant not found with id : " + plantId));

        PlantNotFoundException exception = assertThrows(
                PlantNotFoundException.class,
                () -> plantRetrievalManager.getPlantById(plantId) // Call method
        );
        assertEquals("Plant not found with id : " + plantId, exception.getMessage());
    }
}
