package com.plantNursery.GardeniaDreams.utils.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class PlantDTO {
    private String name;
    private Integer ageInDays;
    private Date lastWateredDate;
    private Integer wateringIntervalInDays;
}
