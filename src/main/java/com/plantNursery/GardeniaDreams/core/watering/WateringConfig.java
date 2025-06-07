package com.plantNursery.GardeniaDreams.core.watering;

import com.plantNursery.GardeniaDreams.core.watering.validator.CompositeWateringRuleValidator;
import com.plantNursery.GardeniaDreams.core.watering.validator.TimeBasedWateringRule;
import com.plantNursery.GardeniaDreams.core.watering.validator.WateringRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.util.List;

@Configuration
public class WateringConfig {
    @Bean
    public WateringRule wateringRuleValidator() {
        return new CompositeWateringRuleValidator(List.of(
                new TimeBasedWateringRule(LocalTime.of(6, 0), LocalTime.of(20, 0))
        ));
    }
}
