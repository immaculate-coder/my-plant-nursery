package com.plantNursery.GardeniaDreams.core.watering.strategy;

import com.plantNursery.GardeniaDreams.core.exceptions.WateringNotAllowedException;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FruitBearingWateringStrategyTest {

    private final FruitBearingWateringStrategy strategy = new FruitBearingWateringStrategy();

    @Test
    void canWater_ShouldThrowException_WhenIntervalNotPassed() {
        // Plant was watered just 1 day ago, interval is 3
        Date lastWateredDate = daysAgo(1);
        Plant plant = Plant.builder()
                .lastWateredDate(lastWateredDate)
                .wateringIntervalInDays(3)
                .isFruitBearing(true)
                .build();

        WateringNotAllowedException ex = assertThrows(WateringNotAllowedException.class, () -> {
            strategy.canWater(plant);
        });

        assertEquals("Watering not allowed yet, interval not passed", ex.getMessage());
    }

    @Test
    void canWater_ShouldPass_WhenIntervalPassed() {
        // Plant watered 5 days ago, interval is 3
        Date lastWateredDate = daysAgo(5);
        Plant plant = Plant.builder()
                .lastWateredDate(lastWateredDate)
                .wateringIntervalInDays(3)
                .isFruitBearing(true)
                .build();

        assertDoesNotThrow(() -> strategy.canWater(plant));
    }

    private Date daysAgo(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -days);
        return cal.getTime();
    }
}
