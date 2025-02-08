package com.plantNursery.GardeniaDreams.api.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetAllPlantsResponse(int numPlants, List<PlantResponse> plants) {
}
