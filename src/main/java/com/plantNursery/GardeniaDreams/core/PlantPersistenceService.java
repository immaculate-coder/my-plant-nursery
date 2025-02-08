package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlantPersistenceService implements PlantPersister {
    private PlantStorageManager plantStorageManager;

    @Override
    public String persist(CreatePlantRequest createPlantRequest) {
        return plantStorageManager.persist(createPlantRequest);
    }

    @Override
    public List<Plant> getAllPlants() {
        return plantStorageManager.getAllPlants();
    }
}
