package com.plantNursery.GardeniaDreams.core.watering.strategy;

import com.plantNursery.GardeniaDreams.core.exceptions.WateringNotAllowedException;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class NonFruitBearingWateringStrategyTest {

    private final NonFruitBearingWateringStrategy strategy = new NonFruitBearingWateringStrategy();

    @Test
    void canWater_ShouldThrowException_WhenWateredToday() {
        Date today = new Date();
        Plant plant = Plant.builder()
                .lastWateredDate(today)
                .isFruitBearing(false)
                .build();

        WateringNotAllowedException ex = assertThrows(WateringNotAllowedException.class, () -> {
            strategy.canWater(plant);
        });

        assertEquals("Plant has already been watered today.", ex.getMessage());
    }

    @Test
    void canWater_ShouldPass_WhenWateredOnDifferentDay() {
        Date yesterday = daysAgo(1);
        Plant plant = Plant.builder()
                .lastWateredDate(yesterday)
                .isFruitBearing(false)
                .build();

        assertDoesNotThrow(() -> strategy.canWater(plant));
    }

    private Date daysAgo(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -days);
        return cal.getTime();
    }
}
