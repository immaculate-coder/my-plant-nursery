package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.model.Plant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlantFetcherService implements PlantFetcher {

    private PlantRetrievalManager plantRetrievalManager;

    @Override
    public List<Plant> getAllPlants() {
        return plantRetrievalManager.getAllPlants();
    }

    @Override
    public Plant getPlantById(String id) {
        return plantRetrievalManager.getPlantById(id);
    }
}
