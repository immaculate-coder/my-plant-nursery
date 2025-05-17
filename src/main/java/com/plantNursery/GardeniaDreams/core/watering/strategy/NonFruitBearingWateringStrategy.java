package com.plantNursery.GardeniaDreams.core.watering.strategy;

import java.util.Calendar;
import java.util.Date;

public class NonFruitBearingWateringStrategy implements WateringStrategy{
    @Override
    public boolean canWater(Date lastWateredDate, Integer wateringInterval) {
        Calendar lastWateredCalendar = Calendar.getInstance();
        lastWateredCalendar.setTime(lastWateredDate);

        Calendar todayCalendar = Calendar.getInstance();

        return (
            lastWateredCalendar.get(Calendar.YEAR) != todayCalendar.get(Calendar.YEAR) ||
            lastWateredCalendar.get(Calendar.DAY_OF_YEAR) != todayCalendar.get(Calendar.DAY_OF_YEAR)
        );
    }
}
