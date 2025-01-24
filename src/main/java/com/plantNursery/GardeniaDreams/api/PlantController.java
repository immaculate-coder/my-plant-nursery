package com.plantNursery.GardeniaDreams.api;

import com.plantNursery.GardeniaDreams.api.request.PlantApiRequest;
import com.plantNursery.GardeniaDreams.core.PlantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/plants")
@AllArgsConstructor
public class PlantController {
    private PlantService plantService;

    ResponseEntity<String> persist(PlantApiRequest request) {
        plantService.persist(request);
        return ResponseEntity.ok("Plant saved successfully");
    }
}
