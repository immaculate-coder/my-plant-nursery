package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.exceptions.WateringNotAllowedException;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import com.plantNursery.GardeniaDreams.core.model.UpdatePlantRequest;
import com.plantNursery.GardeniaDreams.core.watering.factory.WateringStrategyFactory;
import com.plantNursery.GardeniaDreams.core.watering.strategy.WateringStrategy;
import com.plantNursery.GardeniaDreams.core.watering.validator.WateringRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PlantIrrigatorServiceTest {

    private PlantRetrievalManager plantRetrievalManager;
    private PlantStorageManager plantStorageManager;
    private WateringRule wateringRuleValidator;
    private PlantIrrigatorService plantIrrigatorService;

    @BeforeEach
    void setUp() {
        plantRetrievalManager = mock(PlantRetrievalManager.class);
        plantStorageManager = mock(PlantStorageManager.class);
        wateringRuleValidator = mock(WateringRule.class);
        plantIrrigatorService = new PlantIrrigatorService(plantRetrievalManager, plantStorageManager, wateringRuleValidator);
    }

    @Test
    void waterPlant_shouldReturnUpdatedPlant_whenFruitBearing() {
        String plantId = "fruit-plant-1";
        Plant plant = Plant.builder()
                .id(plantId)
                .name("Mango")
                .ageInDays(100)
                .lastWateredDate(daysAgo(3))
                .wateringIntervalInDays(2)
                .isFruitBearing(true)
                .build();

        when(plantRetrievalManager.getPlantById(plantId)).thenReturn(plant);
        doNothing().when(wateringRuleValidator).validate(plant);

        WateringStrategy mockStrategy = mock(WateringStrategy.class);
        doNothing().when(mockStrategy).canWater(plant);

        try (MockedStatic<WateringStrategyFactory> mockedStatic = mockStatic(WateringStrategyFactory.class)) {
            mockedStatic.when(() -> WateringStrategyFactory.getStrategy(plant)).thenReturn(mockStrategy);

            Plant updatedPlantMock = Plant.builder()
                    .id(plantId)
                    .name("Mango")
                    .ageInDays(100)
                    .lastWateredDate(new Date())
                    .wateringIntervalInDays(2)
                    .isFruitBearing(true)
                    .build();

            when(plantStorageManager.updatePlant(eq(plantId), any(UpdatePlantRequest.class)))
                    .thenReturn(updatedPlantMock);

            Plant updatedPlant = plantIrrigatorService.waterPlant(plantId);

            verify(plantRetrievalManager).getPlantById(plantId);
            verify(wateringRuleValidator).validate(plant);
            verify(mockStrategy).canWater(plant);
            verify(plantStorageManager).updatePlant(eq(plantId), any(UpdatePlantRequest.class));

            assertEquals(plantId, updatedPlant.id());
            assertEquals(plant.name(), updatedPlant.name());
            assertTrue(updatedPlant.lastWateredDate().after(plant.lastWateredDate()));
        }
    }

    @Test
    void waterPlant_shouldReturnUpdatedPlant_whenNonFruitBearing() {
        String plantId = "non-fruit-plant-1";
        Plant plant = Plant.builder()
                .id(plantId)
                .name("Fern")
                .ageInDays(50)
                .lastWateredDate(daysAgo(1))
                .wateringIntervalInDays(1)
                .isFruitBearing(false)
                .build();

        when(plantRetrievalManager.getPlantById(plantId)).thenReturn(plant);
        doNothing().when(wateringRuleValidator).validate(plant);

        WateringStrategy mockStrategy = mock(WateringStrategy.class);
        doNothing().when(mockStrategy).canWater(plant);

        try (MockedStatic<WateringStrategyFactory> mockedStatic = mockStatic(WateringStrategyFactory.class)) {
            mockedStatic.when(() -> WateringStrategyFactory.getStrategy(plant)).thenReturn(mockStrategy);

            Plant updatedPlantMock = Plant.builder()
                    .id(plantId)
                    .name("Fern")
                    .ageInDays(50)
                    .lastWateredDate(new Date())
                    .wateringIntervalInDays(1)
                    .isFruitBearing(false)
                    .build();

            when(plantStorageManager.updatePlant(eq(plantId), any(UpdatePlantRequest.class)))
                    .thenReturn(updatedPlantMock);

            Plant updatedPlant = plantIrrigatorService.waterPlant(plantId);

            verify(plantRetrievalManager).getPlantById(plantId);
            verify(wateringRuleValidator).validate(plant);
            verify(mockStrategy).canWater(plant);
            verify(plantStorageManager).updatePlant(eq(plantId), any(UpdatePlantRequest.class));

            assertEquals(plantId, updatedPlant.id());
            assertEquals(plant.name(), updatedPlant.name());
            assertTrue(updatedPlant.lastWateredDate().after(plant.lastWateredDate()));
        }
    }

    @Test
    void waterPlant_shouldThrowWateringNotAllowedException_whenRuleValidatorFails() {
        String plantId = "plant-123";
        Plant plant = Plant.builder()
                .id(plantId)
                .name("Rose")
                .ageInDays(10)
                .lastWateredDate(new Date())
                .wateringIntervalInDays(1)
                .isFruitBearing(false)
                .build();

        when(plantRetrievalManager.getPlantById(plantId)).thenReturn(plant);

        doThrow(new WateringNotAllowedException("Plant cannot be watered at night time"))
                .when(wateringRuleValidator).validate(plant);

        WateringNotAllowedException exception = assertThrows(
                WateringNotAllowedException.class,
                () -> plantIrrigatorService.waterPlant(plantId)
        );

        assertEquals("Plant cannot be watered at night time", exception.getMessage());
        verify(plantRetrievalManager).getPlantById(plantId);
        verify(wateringRuleValidator).validate(plant);
        verifyNoInteractions(plantStorageManager);
    }

    @Test
    void waterPlant_shouldThrowWateringNotAllowedException_whenStrategyPreventsWatering() {
        String plantId = "plant-456";
        Plant plant = Plant.builder()
                .id(plantId)
                .name("Lily")
                .ageInDays(15)
                .lastWateredDate(daysAgo(1))
                .wateringIntervalInDays(3)
                .isFruitBearing(true)
                .build();

        when(plantRetrievalManager.getPlantById(plantId)).thenReturn(plant);
        doNothing().when(wateringRuleValidator).validate(plant);

        WateringStrategy mockStrategy = mock(WateringStrategy.class);
        doThrow(new WateringNotAllowedException("Watering not allowed yet, interval not passed"))
                .when(mockStrategy).canWater(plant);

        try (MockedStatic<WateringStrategyFactory> mockedStatic = mockStatic(WateringStrategyFactory.class)) {
            mockedStatic.when(() -> WateringStrategyFactory.getStrategy(plant)).thenReturn(mockStrategy);

            WateringNotAllowedException exception = assertThrows(
                    WateringNotAllowedException.class,
                    () -> plantIrrigatorService.waterPlant(plantId)
            );

            assertEquals("Watering not allowed yet, interval not passed", exception.getMessage());

            verify(plantRetrievalManager).getPlantById(plantId);
            verify(wateringRuleValidator).validate(plant);
            verify(mockStrategy).canWater(plant);
            verifyNoInteractions(plantStorageManager);
        }
    }

    private static Date daysAgo(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -days);
        return cal.getTime();
    }
}
