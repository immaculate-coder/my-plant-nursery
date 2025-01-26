package com.plantNursery.GardeniaDreams.infra.mongo;

import com.plantNursery.GardeniaDreams.core.PlantStorageManager;
import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import com.plantNursery.GardeniaDreams.infra.mongo.entities.PlantDocument;
import com.plantNursery.GardeniaDreams.infra.mongo.repo.MongoPlantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MongoPlantStorageMangerTest {

    @MockitoBean
    private MongoPlantRepository plantRepository;

    @Autowired
    private PlantStorageManager plantStorageManager;

    @Test
    void persistPlant_shouldReturnPlantId() {
        CreatePlantRequest createPlantRequest = CreatePlantRequest.builder()
                .name("Test Plant")
                .ageInDays(100)
                .lastWateredDate(new Date())
                .wateringIntervalInDays(20)
                .build();

        String expectedPlantId = "dummy-tree-id-123";
        PlantDocument mockPlantDocument = PlantDocument.builder()
                .id(expectedPlantId)
                .name(createPlantRequest.name())
                .ageInDays(createPlantRequest.ageInDays())
                .lastWateredDate(createPlantRequest.lastWateredDate())
                .wateringIntervalInDays(createPlantRequest.wateringIntervalInDays())
                .build();

        when(plantRepository.save(any(PlantDocument.class))).thenReturn(mockPlantDocument);

        String actualPlantId = plantStorageManager.persist(createPlantRequest);

        assertEquals(expectedPlantId, actualPlantId, "Plant id mismatch");
    }

}