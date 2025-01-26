package com.plantNursery.GardeniaDreams.api.request;

import lombok.Builder;

import java.util.Date;

@Builder
public record CreatePlantApiRequest(String name, Integer ageInDays, Date lastWateredDate, Integer wateringIntervalInDays) {
}
