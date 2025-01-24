package com.plantNursery.GardeniaDreams.api.request;

import java.util.Date;

public record PlantApiRequest(String name, Integer ageInDays, Date lastWateredDate, Integer wateringIntervalInDays) {
}
