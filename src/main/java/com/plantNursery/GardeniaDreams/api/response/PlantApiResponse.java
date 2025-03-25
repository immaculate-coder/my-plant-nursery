package com.plantNursery.GardeniaDreams.api.response;

import lombok.Builder;

import java.util.Date;

@Builder
public record PlantApiResponse(String id, String name, Integer ageInDays, Date lastWateredDate, Integer wateringIntervalInDays) {
}
