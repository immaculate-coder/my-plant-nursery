package com.plantNursery.GardeniaDreams.api;

import com.plantNursery.GardeniaDreams.api.request.CreatePlantApiRequest;
import com.plantNursery.GardeniaDreams.api.response.CreatePlantApiResponse;
import com.plantNursery.GardeniaDreams.api.response.GetAllPlantsResponse;
import com.plantNursery.GardeniaDreams.api.response.PlantResponse;
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
            summary = "get all plant",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Plants fetched successfully")
            }
    )
    @GetMapping
    ResponseEntity<GetAllPlantsResponse> getAllPlants() {
        List<PlantResponse> plants = plantPersister.getAllPlants()
                .stream()
                .map(PlantController::from)
                .toList();
        GetAllPlantsResponse response = GetAllPlantsResponse.builder()
                .numPlants(plants.size())
                .plants(plants)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "get plant by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Plant fetched successfully")
            }
    )
    @GetMapping("/{id}")
    ResponseEntity<PlantResponse> getPlantById(@PathVariable String id) {
        PlantResponse response = from(plantPersister.getPlantById(id));
        return ResponseEntity.ok(response);
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

    private static PlantResponse from(Plant plant) {
        return PlantResponse.builder()
                .id(plant.id())
                .name(plant.name())
                .ageInDays(plant.ageInDays())
                .lastWateredDate(plant.lastWateredDate())
                .wateringIntervalInDays(plant.wateringIntervalInDays())
                .build();
    }

}
