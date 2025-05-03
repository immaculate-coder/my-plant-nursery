package com.plantNursery.GardeniaDreams.infra.mongo;

import com.plantNursery.GardeniaDreams.core.PlantStorageManager;
import com.plantNursery.GardeniaDreams.core.exceptions.PlantNotFoundException;
import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import com.plantNursery.GardeniaDreams.core.model.UpdatePlantRequest;
import com.plantNursery.GardeniaDreams.infra.mongo.entities.PlantDocument;
import com.plantNursery.GardeniaDreams.infra.mongo.repo.MongoPlantRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MongoPlantStorageManger implements PlantStorageManager {
    private MongoPlantRepository repository;
    private final MongoTemplate mongoTemplate;

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

    @Override
    public Plant updatePlant(String id, UpdatePlantRequest updatePlantRequest) {
        Query query = new Query(Criteria.where("_id").is(id));

        Update update = new Update();
        if (updatePlantRequest.name() != null) update.set("name", updatePlantRequest.name());
        if (updatePlantRequest.ageInDays() != null) update.set("ageInDays", updatePlantRequest.ageInDays());
        if (updatePlantRequest.wateringIntervalInDays() != null) update.set("wateringIntervalInDays", updatePlantRequest.wateringIntervalInDays());
        if (updatePlantRequest.lastWateredDate() != null) update.set("lastWateredDate", updatePlantRequest.lastWateredDate());

        PlantDocument updatedPlant = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), PlantDocument.class);

        if (updatedPlant == null) {
            throw new PlantNotFoundException("Plant not found with id: " + id);
        }

        return from(updatedPlant);
    }

    private static PlantDocument from(CreatePlantRequest createPlantRequest) {
        return PlantDocument.builder()
                .name(createPlantRequest.name())
                .wateringIntervalInDays(createPlantRequest.wateringIntervalInDays())
                .ageInDays(createPlantRequest.ageInDays())
                .lastWateredDate(createPlantRequest.lastWateredDate())
                .isFruitBearing(createPlantRequest.isFruitBearing())
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
