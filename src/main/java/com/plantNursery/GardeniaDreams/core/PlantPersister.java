package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import com.plantNursery.GardeniaDreams.core.model.Plant;

import java.util.List;

public interface PlantPersister {
    String persist(CreatePlantRequest createPlantRequest);

    List<Plant> getAllPlants();

    Plant getPlantById(String id);
}
