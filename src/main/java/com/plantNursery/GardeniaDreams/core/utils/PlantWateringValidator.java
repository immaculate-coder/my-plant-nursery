package com.plantNursery.GardeniaDreams.core.utils;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class PlantWateringValidator {
    public static boolean canWaterPlant(LocalTime now, LocalTime start, LocalTime end) {
        return now.isAfter(start) && now.isBefore(end);
    }

    public static boolean canWaterFruitBearingPlant(Date lastWateredDate, Integer wateringInterval) {
        Calendar lastWateredCalendar = Calendar.getInstance();
        lastWateredCalendar.setTime(lastWateredDate);

        Calendar todayCalendar = Calendar.getInstance();

        long millisDifference = todayCalendar.getTimeInMillis() - lastWateredCalendar.getTimeInMillis();
        long daysDifference = millisDifference / (1000 * 60 * 60 * 24);

        return daysDifference < wateringInterval;
    }

    public static boolean canWaterNonFruitBearingPlant(Date lastWateredDate) {
        Calendar lastWateredCalendar = Calendar.getInstance();
        lastWateredCalendar.setTime(lastWateredDate);

        Calendar todayCalendar = Calendar.getInstance();

        return lastWateredCalendar.get(Calendar.YEAR) != todayCalendar.get(Calendar.YEAR) ||
                lastWateredCalendar.get(Calendar.DAY_OF_YEAR) != todayCalendar.get(Calendar.DAY_OF_YEAR);
    }
}
