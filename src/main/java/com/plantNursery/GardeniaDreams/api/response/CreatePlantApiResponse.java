package com.plantNursery.GardeniaDreams.api.response;

import lombok.Builder;

@Builder
public record CreatePlantApiResponse(String id, String message) {
}
