package com.plantNursery.GardeniaDreams.core.watering.validator;

import com.plantNursery.GardeniaDreams.core.model.Plant;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CompositeWateringRuleValidator implements WateringRule {
    private final List<WateringRule> rules;

    @Override
    public void validate(Plant plant) {
        for (WateringRule rule : rules) {
            rule.validate(plant);
        }
    }
}