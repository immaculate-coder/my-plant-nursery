package com.plantNursery.GardeniaDreams.core.watering.strategy;

import java.util.Calendar;
import java.util.Date;

public class FruitBearingWateringStrategy implements WateringStrategy{
    @Override
    public boolean canWater(Date lastWateredDate, Integer wateringInterval) {
        Calendar lastWateredCalendar = Calendar.getInstance();
        lastWateredCalendar.setTime(lastWateredDate);

        Calendar todayCalendar = Calendar.getInstance();

        long millisDifference = todayCalendar.getTimeInMillis() - lastWateredCalendar.getTimeInMillis();
        long daysDifference = millisDifference / (1000 * 60 * 60 * 24);
        return daysDifference > wateringInterval;
    }
}
