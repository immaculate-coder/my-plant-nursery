package com.plantNursery.GardeniaDreams.core.watering.factory;

import com.plantNursery.GardeniaDreams.core.model.Plant;
import com.plantNursery.GardeniaDreams.core.watering.strategy.FruitBearingWateringStrategy;
import com.plantNursery.GardeniaDreams.core.watering.strategy.NonFruitBearingWateringStrategy;
import com.plantNursery.GardeniaDreams.core.watering.strategy.WateringStrategy;

public class WateringStrategyFactory {
    public static WateringStrategy getStrategy(Plant plant) {
        return plant.isFruitBearing() ? new FruitBearingWateringStrategy() : new NonFruitBearingWateringStrategy();
    }
}
