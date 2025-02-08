package com.plantNursery.GardeniaDreams.infra.mongo;

import com.plantNursery.GardeniaDreams.core.PlantStorageManager;
import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import com.plantNursery.GardeniaDreams.infra.mongo.entities.PlantDocument;
import com.plantNursery.GardeniaDreams.infra.mongo.repo.MongoPlantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MongoPlantStorageManger implements PlantStorageManager {
    private MongoPlantRepository repository;
    @Override
    public String persist(CreatePlantRequest createPlantRequest) {
        return repository.save(from(createPlantRequest)).getId();
    }

    @Override
    public List<Plant> getAllPlants() {
        List<PlantDocument> plantDocuments = repository.findAll();
        return plantDocuments.stream().map(MongoPlantStorageManger::from).toList();
    }

    private static PlantDocument from(CreatePlantRequest createPlantRequest) {
        return PlantDocument.builder()
                .name(createPlantRequest.name())
                .wateringIntervalInDays(createPlantRequest.wateringIntervalInDays())
                .ageInDays(createPlantRequest.ageInDays())
                .lastWateredDate(createPlantRequest.lastWateredDate())
                .build();
    }

    private static Plant from(PlantDocument plantDocument) {
        return Plant.builder()
                .id(plantDocument.getId())
                .name(plantDocument.getName())
                .wateringIntervalInDays(plantDocument.getWateringIntervalInDays())
                .ageInDays(plantDocument.getAgeInDays())
                .lastWateredDate(plantDocument.getLastWateredDate())
                .build();
    }
}
