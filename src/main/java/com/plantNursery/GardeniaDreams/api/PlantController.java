package com.plantNursery.GardeniaDreams.api;

import com.plantNursery.GardeniaDreams.api.request.CreatePlantApiRequest;
import com.plantNursery.GardeniaDreams.api.response.CreatePlantApiResponse;
import com.plantNursery.GardeniaDreams.core.PlantPersister;
import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/plants")
@AllArgsConstructor
public class PlantController {
    private PlantPersister plantService;

    @Operation(
            summary = "Persist a new plant",
            responses = {
            @ApiResponse(responseCode = "201", description = "Plant saved successfully")
        }
    )
    @PostMapping
    ResponseEntity<CreatePlantApiResponse> persistNewPlant(@RequestBody CreatePlantApiRequest createPlantApiRequest) {
        plantService.persist(from(createPlantApiRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(from("dummy-id-123"));
    }


    // mappers -> once it increases as api request increases this can be abstracted to separate file

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
}
