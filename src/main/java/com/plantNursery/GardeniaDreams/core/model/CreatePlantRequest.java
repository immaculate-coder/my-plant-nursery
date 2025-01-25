package com.plantNursery.GardeniaDreams.core.model;


import lombok.Builder;

import java.util.Date;

@Builder
public record CreatePlantRequest(String name, Integer ageInDays, Date lastWateredDate, Integer wateringIntervalInDays) {
}