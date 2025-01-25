package com.plantNursery.GardeniaDreams.infra.mongo;

import com.plantNursery.GardeniaDreams.core.PlantStorageManager;
import com.plantNursery.GardeniaDreams.infra.mongo.entities.PlantDocument;
import com.plantNursery.GardeniaDreams.infra.mongo.repo.MongoPlantRepository;
import com.plantNursery.GardeniaDreams.utils.dto.PlantDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MongoPlantStorageManger implements PlantStorageManager {
    private MongoPlantRepository repository;
    @Override
    public String persist(PlantDTO plantDTO) {
        return repository.save(from(plantDTO)).getId();
    }

    // mappers
    private static PlantDocument from(PlantDTO plantDTO) {
        return PlantDocument.builder()
                .name(plantDTO.getName())
                .wateringIntervalInDays(plantDTO.getWateringIntervalInDays())
                .ageInDays(plantDTO.getAgeInDays())
                .lastWateredDate(plantDTO.getLastWateredDate())
                .build();
    }
}
