package com.plantNursery.GardeniaDreams.api.request;

import java.util.Date;

public record CreatePlantApiRequest(String name, Integer ageInDays, Date lastWateredDate, Integer wateringIntervalInDays) {
}
