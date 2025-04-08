package com.plantNursery.GardeniaDreams.core.model;

import lombok.Builder;

import java.util.Date;

@Builder
public record UpdatePlantRequest(String name, Integer ageInDays, Date lastWateredDate, Integer wateringIntervalInDays) {
}