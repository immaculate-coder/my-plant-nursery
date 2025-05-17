package com.plantNursery.GardeniaDreams.core.utils;

import java.time.LocalTime;

public class PlantWateringValidator {
    public static boolean canWaterPlant(LocalTime now, LocalTime start, LocalTime end) {
        return now.isAfter(start) && now.isBefore(end);
    }
}
