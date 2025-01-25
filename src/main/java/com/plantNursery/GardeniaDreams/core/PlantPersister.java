package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;

public interface PlantPersister {
    String persist(CreatePlantRequest createPlantRequest);
}
