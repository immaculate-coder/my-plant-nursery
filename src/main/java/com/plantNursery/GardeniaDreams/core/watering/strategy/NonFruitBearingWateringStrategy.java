package com.plantNursery.GardeniaDreams.core.watering.strategy;

import com.plantNursery.GardeniaDreams.core.exceptions.WateringNotAllowedException;
import com.plantNursery.GardeniaDreams.core.model.Plant;

import java.util.Calendar;
import java.util.Date;

public class NonFruitBearingWateringStrategy implements WateringStrategy{
    @Override
    public void canWater(Plant plant) {
        Date lastWateredDate = plant.lastWateredDate();

        Calendar lastWateredCalendar = Calendar.getInstance();
        lastWateredCalendar.setTime(lastWateredDate);

        Calendar todayCalendar = Calendar.getInstance();

        boolean isDifferentDay = (
            lastWateredCalendar.get(Calendar.YEAR) != todayCalendar.get(Calendar.YEAR) ||
            lastWateredCalendar.get(Calendar.DAY_OF_YEAR) != todayCalendar.get(Calendar.DAY_OF_YEAR)
        );

        if(!isDifferentDay) {
            throw new WateringNotAllowedException("Plant has already been watered today.");
        }
    }
}
