package com.plantNursery.GardeniaDreams.api.response;

import lombok.Builder;

import java.util.Date;

@Builder
public record PlantResponse(String id, String name, Integer ageInDays, Date lastWateredDate, Integer wateringIntervalInDays) {
}
