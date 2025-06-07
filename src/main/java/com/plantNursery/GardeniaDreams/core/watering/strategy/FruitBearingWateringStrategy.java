package com.plantNursery.GardeniaDreams.core.watering.strategy;

import com.plantNursery.GardeniaDreams.core.exceptions.WateringNotAllowedException;
import com.plantNursery.GardeniaDreams.core.model.Plant;

import java.util.Calendar;
import java.util.Date;

public class FruitBearingWateringStrategy implements WateringStrategy{
    @Override
    public void canWater(Plant plant) {
        Date lastWateredDate = plant.lastWateredDate();
        int wateringInterval = plant.wateringIntervalInDays();

        Calendar lastWateredCalendar = Calendar.getInstance();
        lastWateredCalendar.setTime(lastWateredDate);

        Calendar todayCalendar = Calendar.getInstance();

        long millisDifference = todayCalendar.getTimeInMillis() - lastWateredCalendar.getTimeInMillis();
        long daysDifference = millisDifference / (1000 * 60 * 60 * 24);
        if(daysDifference <= wateringInterval) {
            throw new WateringNotAllowedException("Watering not allowed yet, interval not passed");
        }
    }
}
