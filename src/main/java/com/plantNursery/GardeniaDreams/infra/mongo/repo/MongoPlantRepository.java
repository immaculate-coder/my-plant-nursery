package com.plantNursery.GardeniaDreams.infra.mongo.repo;

import com.plantNursery.GardeniaDreams.infra.mongo.entities.PlantDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoPlantRepository extends MongoRepository<PlantDocument, String> {
}
