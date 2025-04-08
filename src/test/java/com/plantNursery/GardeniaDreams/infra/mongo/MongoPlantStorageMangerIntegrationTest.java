package com.plantNursery.GardeniaDreams.infra.mongo;

import com.plantNursery.GardeniaDreams.core.exceptions.PlantNotFoundException;
import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import com.plantNursery.GardeniaDreams.core.model.UpdatePlantRequest;
import com.plantNursery.GardeniaDreams.infra.mongo.entities.PlantDocument;
import com.plantNursery.GardeniaDreams.infra.mongo.repo.MongoPlantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers
class MongoPlantStorageMangerIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MongoPlantStorageManger storageManger;

    @Autowired
    private MongoPlantRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void persistPlant_ShouldReturnId() {
        CreatePlantRequest createPlantRequest = getTestPlantRequest();
        String id = storageManger.persist(createPlantRequest);
        assertThat(id).isNotNull();

        PlantDocument savedDocument = repository.findById(id)
                .orElseThrow(() -> new AssertionError("PlantDocument not found for id: " + id));

        assertThat(savedDocument.getName()).isEqualTo(createPlantRequest.name());
        assertThat(savedDocument.getWateringIntervalInDays()).isEqualTo(createPlantRequest.wateringIntervalInDays());
        assertThat(savedDocument.getAgeInDays()).isEqualTo(createPlantRequest.ageInDays());
        assertThat(savedDocument.getLastWateredDate()).isEqualTo(createPlantRequest.lastWateredDate());
    }

    @Test
    void deletePlant_ShouldDeletePlant() {
        CreatePlantRequest createPlantRequest = getTestPlantRequest();
        String testPlantId = repository.save(getTestPlantDocument(createPlantRequest)).getId();
        assertThat(testPlantId).isNotNull();

        String deletedPlantId = storageManger.deletePlant(testPlantId);
        boolean isPlantPresent = repository.existsById(deletedPlantId);

        assertThat(testPlantId).isEqualTo(deletedPlantId);
        assertThat(isPlantPresent).isEqualTo(false);
    }

    @Test
    void deletePlant_shouldThrowPlantNotFoundException() {
        String testPlantId = "invalid-plant-id";

        assertThatThrownBy(() -> storageManger.deletePlant(testPlantId))
                .isInstanceOf(PlantNotFoundException.class)
                .hasMessage("Plant not found with id : " + testPlantId);

    }

    @Test
    void updatePlant_shouldUpdateAndReturnPlant() {
        CreatePlantRequest createRequest = getTestPlantRequest();
        PlantDocument savedPlant = repository.save(getTestPlantDocument(createRequest));
        String plantId = savedPlant.getId();

        assertThat(plantId).isNotNull();

        UpdatePlantRequest updatePlantRequest = getTestUpdatePlantRequest();

        Plant updatedPlant = storageManger.updatePlant(plantId, updatePlantRequest);

        assertThat(updatedPlant.id()).isEqualTo(plantId);
        assertThat(updatedPlant.name()).isEqualTo(updatePlantRequest.name());
        assertThat(updatedPlant.ageInDays()).isEqualTo(updatePlantRequest.ageInDays());
        assertThat(updatedPlant.wateringIntervalInDays()).isEqualTo(updatePlantRequest.wateringIntervalInDays());
        assertThat(updatedPlant.lastWateredDate()).isEqualTo(updatePlantRequest.lastWateredDate());
    }

    @Test
    void updatePlant_shouldThrowPlantNotFoundException() {
        String testPlantId = "invalid-plant-id";
        UpdatePlantRequest updateRequest = getTestUpdatePlantRequest();

        assertThatThrownBy(() -> storageManger.updatePlant(testPlantId, updateRequest))
                .isInstanceOf(PlantNotFoundException.class)
                .hasMessage("Plant not found with id: " + testPlantId);
    }

    private static UpdatePlantRequest getTestUpdatePlantRequest() {
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        String plantName = "Test Updated Plant";
        int ageInDays = 200;
        int wateringIntervalInDays = 10;

        return UpdatePlantRequest.builder()
                .name(plantName)
                .ageInDays(ageInDays)
                .lastWateredDate(yesterday)
                .wateringIntervalInDays(wateringIntervalInDays)
                .build();
    }

    private static CreatePlantRequest getTestPlantRequest() {
        Date today = new Date();
        String plantName = "Test Plant";
        int ageInDays = 100;
        int wateringIntervalInDays = 20;
        return CreatePlantRequest.builder()
                .name(plantName)
                .ageInDays(ageInDays)
                .lastWateredDate(today)
                .wateringIntervalInDays(wateringIntervalInDays)
                .build();
    }

    private static PlantDocument getTestPlantDocument(CreatePlantRequest createPlantRequest) {
        return PlantDocument.builder()
                .name(createPlantRequest.name())
                .wateringIntervalInDays(createPlantRequest.wateringIntervalInDays())
                .ageInDays(createPlantRequest.ageInDays())
                .lastWateredDate(createPlantRequest.lastWateredDate())
                .build();
    }
}