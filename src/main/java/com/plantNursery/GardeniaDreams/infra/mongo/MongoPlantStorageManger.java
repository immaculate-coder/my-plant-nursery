package com.plantNursery.GardeniaDreams.infra.mongo;

import com.plantNursery.GardeniaDreams.core.PlantStorageManager;
import com.plantNursery.GardeniaDreams.core.exceptions.PlantNotFoundException;
import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import com.plantNursery.GardeniaDreams.infra.mongo.entities.PlantDocument;
import com.plantNursery.GardeniaDreams.infra.mongo.repo.MongoPlantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MongoPlantStorageManger implements PlantStorageManager {
    private MongoPlantRepository repository;
    @Override
    public String persist(CreatePlantRequest createPlantRequest) {
        return repository.save(from(createPlantRequest)).getId();
    }

    @Override
    public String deletePlant(String id) {
        if(!repository.existsById(id)) {
            throw new PlantNotFoundException("Plant not found with id : " + id);
        }
        repository.deleteById(id);
        return id;
    }

    private static PlantDocument from(CreatePlantRequest createPlantRequest) {
        return PlantDocument.builder()
                .name(createPlantRequest.name())
                .wateringIntervalInDays(createPlantRequest.wateringIntervalInDays())
                .ageInDays(createPlantRequest.ageInDays())
                .lastWateredDate(createPlantRequest.lastWateredDate())
                .build();
    }
}
