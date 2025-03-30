package com.plantNursery.GardeniaDreams.infra.mongo;

import com.plantNursery.GardeniaDreams.core.exceptions.PlantNotFoundException;
import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
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