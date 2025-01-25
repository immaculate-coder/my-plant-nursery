package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import com.plantNursery.GardeniaDreams.utils.dto.PlantDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlantPersistenceService implements PlantPersister {
    private PlantStorageManager plantStorageManager;

    @Override
    public void persist(CreatePlantRequest createPlantRequest) {
        PlantDTO plantDTO = PlantDTO.builder()
                .name(createPlantRequest.name())
                .ageInDays(createPlantRequest.ageInDays())
                .lastWateredDate(createPlantRequest.lastWateredDate())
                .wateringIntervalInDays(createPlantRequest.wateringIntervalInDays())
                .build();

        plantStorageManager.persist(plantDTO);
    }
}
