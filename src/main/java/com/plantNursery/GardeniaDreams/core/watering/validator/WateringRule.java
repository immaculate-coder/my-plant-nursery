package com.plantNursery.GardeniaDreams.core.watering.validator;

import com.plantNursery.GardeniaDreams.core.model.Plant;

public interface WateringRule {
    void validate(Plant plant);
}
