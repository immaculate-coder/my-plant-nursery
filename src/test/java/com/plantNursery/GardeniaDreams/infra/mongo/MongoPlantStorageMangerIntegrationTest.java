package com.plantNursery.GardeniaDreams.infra.mongo;

import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import com.plantNursery.GardeniaDreams.infra.mongo.entities.PlantDocument;
import com.plantNursery.GardeniaDreams.infra.mongo.repo.MongoPlantRepository;
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

    @Test
    void testPersistPlant() {
        Date today = new Date();
        String plantName = "Test Plant";
        int ageInDays = 100;
        int wateringIntervalInDays = 20;
        CreatePlantRequest createPlantRequest = CreatePlantRequest.builder()
                .name(plantName)
                .ageInDays(ageInDays)
                .lastWateredDate(today)
                .wateringIntervalInDays(wateringIntervalInDays)
                .build();

        String id = storageManger.persist(createPlantRequest);
        assertThat(id).isNotNull();

        PlantDocument savedDocument = repository.findById(id)
                .orElseThrow(() -> new AssertionError("PlantDocument not found for id: " + id));

        assertThat(savedDocument.getName()).isEqualTo(plantName);
        assertThat(savedDocument.getWateringIntervalInDays()).isEqualTo(wateringIntervalInDays);
        assertThat(savedDocument.getAgeInDays()).isEqualTo(ageInDays);
        assertThat(savedDocument.getLastWateredDate()).isEqualTo(today);
    }
}