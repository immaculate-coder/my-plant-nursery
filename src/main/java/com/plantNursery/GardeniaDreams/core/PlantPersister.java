package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import com.plantNursery.GardeniaDreams.core.model.UpdatePlantRequest;

public interface PlantPersister {
    String persist(CreatePlantRequest createPlantRequest);
    String deletePlant(String id);
    Plant updatePlant(String id, UpdatePlantRequest updatePlantRequest);
}