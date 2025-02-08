package com.plantNursery.GardeniaDreams.infra.mongo;

import com.plantNursery.GardeniaDreams.core.PlantStorageManager;
import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import com.plantNursery.GardeniaDreams.infra.mongo.entities.PlantDocument;
import com.plantNursery.GardeniaDreams.infra.mongo.repo.MongoPlantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Plant getPlantById(String id) {
        Optional<PlantDocument> plantDocument = repository.findById(id);
        if(plantDocument.isPresent()) {
            return from(plantDocument.get());
        } else {
            throw new RuntimeException("Invalid plant Id");
        }
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
