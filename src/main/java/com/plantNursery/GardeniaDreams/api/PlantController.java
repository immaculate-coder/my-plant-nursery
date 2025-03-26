package com.plantNursery.GardeniaDreams.api;

import com.plantNursery.GardeniaDreams.api.request.CreatePlantApiRequest;
import com.plantNursery.GardeniaDreams.api.response.CreatePlantApiResponse;
import com.plantNursery.GardeniaDreams.api.response.GetAllPlantsApiResponse;
import com.plantNursery.GardeniaDreams.api.response.PlantApiResponse;
import com.plantNursery.GardeniaDreams.core.PlantFetcher;
import com.plantNursery.GardeniaDreams.core.PlantPersister;
import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/plants")
@AllArgsConstructor
public class PlantController {
    private PlantPersister plantPersister;
    private PlantFetcher plantFetcher;

    @Operation(
            summary = "Persist a new plant",
            responses = {
            @ApiResponse(responseCode = "201", description = "Plant saved successfully")
        }
    )
    @PostMapping
    ResponseEntity<CreatePlantApiResponse> createNewPlant(@RequestBody CreatePlantApiRequest createPlantApiRequest) {
        String createdPlantId = plantPersister.persist(from(createPlantApiRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(from(createdPlantId));
    }

    @Operation(
            summary = "get all plants",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Plants fetched successfully")
            }
    )
    @GetMapping
    ResponseEntity<GetAllPlantsApiResponse> getAllPlants() {
        List<PlantApiResponse> plants = plantFetcher.getAllPlants()
                .stream()
                .map(PlantController::from)
                .toList();

        return ResponseEntity.ok(from(plants));
    }

    @Operation(
            summary = "get plant by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Plant fetched successfully"),
                    @ApiResponse(responseCode = "404", description = "Plant not found")
            }
    )
    @GetMapping("/{id}")
    ResponseEntity<PlantApiResponse> getPlantById(@PathVariable String id) {
        PlantApiResponse response = from(plantFetcher.getPlantById(id));
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "delete plant by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Plant deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Plant not found")
            }
    )
    @DeleteMapping("/{id}")
    ResponseEntity<String> deletePlantById(@PathVariable String id) {
        String deletedPlantId = plantPersister.deletePlant(id);
        return ResponseEntity.ok("Plant deleted successfully with id : " + deletedPlantId);
    }

    private static CreatePlantRequest from(CreatePlantApiRequest createPlantApiRequest) {
        return CreatePlantRequest.builder()
                .name(createPlantApiRequest.name())
                .ageInDays(createPlantApiRequest.ageInDays())
                .lastWateredDate(createPlantApiRequest.lastWateredDate())
                .wateringIntervalInDays(createPlantApiRequest.wateringIntervalInDays())
                .build();
    }

    private static CreatePlantApiResponse from(String id) {
        return CreatePlantApiResponse.builder()
                .id(id)
                .message("Plant saved successfully")
                .build();
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

    private static GetAllPlantsApiResponse from(List<PlantApiResponse> plants) {
        return GetAllPlantsApiResponse.builder()
                .plantsCount(plants.size())
                .plants(plants)
                .build();
    }

}
