package com.plantNursery.GardeniaDreams.infra.mongo;

import com.plantNursery.GardeniaDreams.core.PlantRetrievalManager;
import com.plantNursery.GardeniaDreams.core.exceptions.PlantNotFoundException;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import com.plantNursery.GardeniaDreams.infra.mongo.entities.PlantDocument;
import com.plantNursery.GardeniaDreams.infra.mongo.repo.MongoPlantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MongoPlantRetrievalManager implements PlantRetrievalManager {
    private MongoPlantRepository repository;

    @Override
    public List<Plant> getAllPlants() {
        List<PlantDocument> plantDocuments = repository.findAll();
        return plantDocuments.stream().map(MongoPlantRetrievalManager::from).toList();
    }

    @Override
    public Plant getPlantById(String id) {
        Optional<PlantDocument> plantDocument = repository.findById(id);
        if(plantDocument.isPresent()) {
            return from(plantDocument.get());
        } else {
            throw new PlantNotFoundException("Plant not found with id : " + id);
        }
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
