package com.plantNursery.GardeniaDreams.api.request;

import lombok.Builder;

import java.util.Date;

@Builder
public record UpdatePlantApiRequest(String name, Integer ageInDays, Date lastWateredDate, Integer wateringIntervalInDays) {
}
