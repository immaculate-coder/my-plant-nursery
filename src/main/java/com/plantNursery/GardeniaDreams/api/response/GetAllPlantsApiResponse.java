package com.plantNursery.GardeniaDreams.api.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetAllPlantsApiResponse(int plantsCount, List<PlantApiResponse> plants) {
}
