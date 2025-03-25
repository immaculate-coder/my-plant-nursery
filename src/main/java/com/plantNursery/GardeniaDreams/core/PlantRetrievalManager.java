package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.model.Plant;

import java.util.List;

public interface PlantRetrievalManager {
    List<Plant> getAllPlants();

    Plant getPlantById(String id);
}
