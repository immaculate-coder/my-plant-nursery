package com.plantNursery.GardeniaDreams.api;

import com.plantNursery.GardeniaDreams.api.response.PlantApiResponse;
import com.plantNursery.GardeniaDreams.core.PlantIrrigator;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/plants")
@AllArgsConstructor
public class PlantWaterController {
    private PlantIrrigator plantIrrigator;

    @Operation(
            summary = "water plant",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Plant watered successfully"),
                    @ApiResponse(responseCode = "404", description = "Plant not found"),
                    @ApiResponse(responseCode = "409", description = "Watering not allowed")
            }
    )
    @PostMapping("/{id}/waterPlant")
    ResponseEntity<PlantApiResponse> waterPlant(@PathVariable String id) {
        PlantApiResponse response = from(plantIrrigator.waterPlant(id));
        return ResponseEntity.ok(response);
    }

    private static PlantApiResponse from(Plant plant) {
        return PlantApiResponse.builder()
                .id(plant.id())
                .name(plant.name())
                .ageInDays(plant.ageInDays())
                .lastWateredDate(plant.lastWateredDate())
                .wateringIntervalInDays(plant.wateringIntervalInDays())
                .build();
    }
}
