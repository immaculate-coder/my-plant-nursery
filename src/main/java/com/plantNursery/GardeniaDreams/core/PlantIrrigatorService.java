package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.exceptions.WateringNotAllowedException;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import com.plantNursery.GardeniaDreams.core.model.UpdatePlantRequest;
import com.plantNursery.GardeniaDreams.core.utils.PlantWateringValidator;
import com.plantNursery.GardeniaDreams.core.watering.factory.WateringStrategyFactory;
import com.plantNursery.GardeniaDreams.core.watering.strategy.WateringStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;

@Service
@AllArgsConstructor
public class PlantIrrigatorService implements PlantIrrigator {
    private PlantRetrievalManager plantRetrievalManager;
    private PlantStorageManager plantStorageManager;

    @Override
    public Plant waterPlant(String plantId) {
        boolean isWateringPossible = PlantWateringValidator.canWaterPlant(LocalTime.now(), LocalTime.of(6, 0), LocalTime.of(20, 0));
        if(!isWateringPossible) {
            throw new WateringNotAllowedException("Plant cannot be watered at night time");
        }

        Plant plant = plantRetrievalManager.getPlantById(plantId);

        WateringStrategy strategy = WateringStrategyFactory.getStrategy(plant.isFruitBearing());
        boolean canWater = strategy.canWater(plant.lastWateredDate(), plant.wateringIntervalInDays());
        if(!canWater) {
            throw new WateringNotAllowedException("Watering not allowed based on strategy");
        }

        return plantStorageManager.updatePlant(plantId,toUpdatePlantRequest(plant));
    }

    private static UpdatePlantRequest toUpdatePlantRequest(Plant plant) {
        return UpdatePlantRequest.builder()
                .name(plant.name())
                .ageInDays(plant.ageInDays())
                .lastWateredDate(new Date())
                .wateringIntervalInDays(plant.wateringIntervalInDays())
                .isFruitBearing(plant.isFruitBearing())
                .build();
    }
}
