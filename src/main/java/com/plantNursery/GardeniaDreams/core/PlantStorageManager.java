package com.plantNursery.GardeniaDreams.core;


import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;

public interface PlantStorageManager {
    String persist(CreatePlantRequest createPlantRequest);
}
