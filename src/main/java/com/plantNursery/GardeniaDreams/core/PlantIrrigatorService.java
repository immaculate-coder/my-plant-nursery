package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.model.Plant;
import com.plantNursery.GardeniaDreams.core.model.UpdatePlantRequest;
import com.plantNursery.GardeniaDreams.core.watering.factory.WateringStrategyFactory;
import com.plantNursery.GardeniaDreams.core.watering.strategy.WateringStrategy;
import com.plantNursery.GardeniaDreams.core.watering.validator.WateringRule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;

@Service
@AllArgsConstructor
public class PlantIrrigatorService implements PlantIrrigator {
    private PlantRetrievalManager plantRetrievalManager;
    private PlantStorageManager plantStorageManager;
    private WateringRule wateringRuleValidator;

    @Override
    public Plant waterPlant(String plantId) {

        Plant plant = plantRetrievalManager.getPlantById(plantId);
        wateringRuleValidator.validate(plant);

        WateringStrategy strategy = WateringStrategyFactory.getStrategy(plant);
        strategy.canWater(plant);

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
