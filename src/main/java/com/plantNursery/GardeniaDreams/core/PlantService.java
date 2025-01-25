package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;

public interface PlantService {
    void persist(CreatePlantRequest createPlantRequest);
}
