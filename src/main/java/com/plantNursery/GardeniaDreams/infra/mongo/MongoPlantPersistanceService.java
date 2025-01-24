package com.plantNursery.GardeniaDreams.infra.mongo;

import com.plantNursery.GardeniaDreams.core.PlantPersistanceService;
import com.plantNursery.GardeniaDreams.infra.mongo.entities.PlantDocument;
import com.plantNursery.GardeniaDreams.infra.mongo.repo.MongoPlantRepository;
import com.plantNursery.GardeniaDreams.utils.dto.PlantDTO;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Primary
public class MongoPlantPersistanceService implements PlantPersistanceService {
    private MongoPlantRepository repository;
    @Override
    public void persist(PlantDTO plantDTO) {
        PlantDocument plantDocument = PlantDocument.builder()
                .name(plantDTO.getName())
                .wateringIntervalInDays(plantDTO.getWateringIntervalInDays())
                .ageInDays(plantDTO.getAgeInDays())
                .lastWateredDate(plantDTO.getLastWateredDate())
                .build();

        repository.save(plantDocument);
    }
}
