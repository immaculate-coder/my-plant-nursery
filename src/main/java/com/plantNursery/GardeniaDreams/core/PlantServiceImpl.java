package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.api.request.PlantApiRequest;
import com.plantNursery.GardeniaDreams.utils.dto.PlantDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlantServiceImpl implements PlantService{
    private PlantPersistanceService plantPersistanceService;

    @Override
    public void persist(PlantApiRequest plantApiRequest) {
        PlantDTO plantDTO = PlantDTO.builder()
                .name(plantApiRequest.name())
                .ageInDays(plantApiRequest.ageInDays())
                .lastWateredDate(plantApiRequest.lastWateredDate())
                .wateringIntervalInDays(plantApiRequest.wateringIntervalInDays())
                .build();

        plantPersistanceService.persist(plantDTO);
    }
}
