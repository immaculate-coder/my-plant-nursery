package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.exceptions.PlantNotWateredException;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import com.plantNursery.GardeniaDreams.core.model.UpdatePlantRequest;
import com.plantNursery.GardeniaDreams.core.utils.PlantWateringValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;

@Service
@AllArgsConstructor
public class PlantOperationsService implements PlantOperations{
    private PlantRetrievalManager plantRetrievalManager;
    private PlantStorageManager plantStorageManager;

    @Override
    public Plant waterPlant(String plantId) {
        boolean isWateringPossible = PlantWateringValidator.canWaterPlant(LocalTime.now(), LocalTime.of(6, 0), LocalTime.of(20, 0));
        if(!isWateringPossible) {
            throw new PlantNotWateredException("Plant cannot be watered at night time");
        }

        Plant plant = plantRetrievalManager.getPlantById(plantId);
        UpdatePlantRequest updatePlantRequest = new UpdatePlantRequest(plant.name(), plant.ageInDays(), new Date(), plant.wateringIntervalInDays(), plant.isFruitBearing());
        if(plant.isFruitBearing() &&
           !PlantWateringValidator.canWaterFruitBearingPlant(plant.lastWateredDate(), plant.wateringIntervalInDays())
        ) {
            throw new PlantNotWateredException("Fruit bearing plant cannot be watered multiple times within watering interval");
        } else if(!PlantWateringValidator.canWaterNonFruitBearingPlant(plant.lastWateredDate())) {
            throw new PlantNotWateredException("Non fruit bearing plant cannot be watered multiple times in same day");
        }

        return plantStorageManager.updatePlant(plantId,updatePlantRequest);
    }
}
