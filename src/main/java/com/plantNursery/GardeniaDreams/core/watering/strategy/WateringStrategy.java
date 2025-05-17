package com.plantNursery.GardeniaDreams.core.watering.strategy;

import java.util.Date;

public interface WateringStrategy {
    boolean canWater(Date lastWateredDate, Integer wateringInterval);
}
