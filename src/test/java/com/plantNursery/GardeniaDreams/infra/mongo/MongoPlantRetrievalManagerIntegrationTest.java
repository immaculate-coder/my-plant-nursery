package com.plantNursery.GardeniaDreams.infra.mongo;

import com.plantNursery.GardeniaDreams.core.exceptions.PlantNotFoundException;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import com.plantNursery.GardeniaDreams.infra.mongo.entities.PlantDocument;
import com.plantNursery.GardeniaDreams.infra.mongo.repo.MongoPlantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
public class MongoPlantRetrievalManagerIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MongoPlantRetrievalManager retrievalManager;

    @Autowired
    private MongoPlantRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void getAllPlants_ShouldReturnList() {
        PlantDocument plant1 = PlantDocument.builder()
                .name("Rose")
                .ageInDays(50)
                .wateringIntervalInDays(7)
                .lastWateredDate(new Date())
                .build();

        PlantDocument plant2 = PlantDocument.builder()
                .name("Tulip")
                .ageInDays(30)
                .wateringIntervalInDays(5)
                .lastWateredDate(new Date())
                .build();

        repository.saveAll(List.of(plant1, plant2));

        List<Plant> plants = retrievalManager.getAllPlants();
        assertThat(plants).hasSize(2);
        assertThat(plants).extracting(Plant::name).containsExactlyInAnyOrder("Rose", "Tulip");
    }

    @Test
    void getPlantById_ShouldReturnPlant() {
        PlantDocument plant = PlantDocument.builder()
                .name("Cactus")
                .ageInDays(120)
                .wateringIntervalInDays(15)
                .lastWateredDate(new Date())
                .build();

        PlantDocument savedPlant = repository.save(plant);

        Plant retrievedPlant = retrievalManager.getPlantById(savedPlant.getId());
        assertThat(retrievedPlant).isNotNull();
        assertThat(retrievedPlant.name()).isEqualTo("Cactus");
        assertThat(retrievedPlant.wateringIntervalInDays()).isEqualTo(15);
    }

    @Test
    void getPlantById_ShouldThrowNotFoundException() {
        String nonExistentId = "invalid-id";

        assertThrows(PlantNotFoundException.class, () -> {
            retrievalManager.getPlantById(nonExistentId);
        });
    }

}
