package com.plantNursery.GardeniaDreams.core.watering.validator;

import com.plantNursery.GardeniaDreams.core.exceptions.WateringNotAllowedException;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import lombok.AllArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
public class TimeBasedWateringRule implements WateringRule {
    private final LocalTime start;
    private final LocalTime end;

    @Override
    public void validate(Plant plant) {
        LocalTime now = LocalTime.now();
        if (now.isBefore(start) || now.isAfter(end)) {
            throw new WateringNotAllowedException("Plant cannot be watered at night time");
        }
    }
}
