package com.plantNursery.GardeniaDreams.infra.mongo.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Builder
@Data
@Document(collection = "plants")
public class PlantDocument {
    @Id
    private String id;
    private String name;
    private Integer ageInDays;
    private Date lastWateredDate;
    private Integer wateringIntervalInDays;
}
