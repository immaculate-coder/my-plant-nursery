package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlantPersistenceService implements PlantPersister {
    private PlantStorageManager plantStorageManager;

    @Override
    public String persist(CreatePlantRequest createPlantRequest) {
        return plantStorageManager.persist(createPlantRequest);
    }

    @Override
    public String deletePlant(String id) {
        throw new UnsupportedOperationException();
    }
}
