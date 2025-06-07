package com.plantNursery.GardeniaDreams.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantNursery.GardeniaDreams.api.response.PlantApiResponse;
import com.plantNursery.GardeniaDreams.core.PlantIrrigator;
import com.plantNursery.GardeniaDreams.core.exceptions.WateringNotAllowedException;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PlantWaterController.class)
public class PlantWaterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlantIrrigator plantIrrigator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void waterPlant_shouldReturnPlantApiResponse_whenSuccessful() throws Exception {
        String plantId = "plant-123";
        Plant mockedPlant = Plant.builder()
                .id(plantId)
                .name("Rose")
                .ageInDays(45)
                .wateringIntervalInDays(6)
                .isFruitBearing(false)
                .lastWateredDate(new Date())
                .build();

        when(plantIrrigator.waterPlant(plantId)).thenReturn(mockedPlant);

        PlantApiResponse expectedResponse = toPlantApiResponse(mockedPlant);

        mockMvc.perform(post("/api/v1/plants/{id}/water", plantId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedResponse.id()))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void waterPlant_shouldReturnConflict_whenWateringNotAllowed() throws Exception {
        String plantId = "test-id-1";
        String errorMessage = "Plant cannot be watered at night time";

        when(plantIrrigator.waterPlant(plantId))
                .thenThrow(new WateringNotAllowedException(errorMessage));

        mockMvc.perform(post("/api/v1/plants/{id}/water", plantId))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }


    private PlantApiResponse toPlantApiResponse(Plant plant) {
        return PlantApiResponse.builder()
                .id(plant.id())
                .name(plant.name())
                .ageInDays(plant.ageInDays())
                .lastWateredDate(plant.lastWateredDate())
                .wateringIntervalInDays(plant.wateringIntervalInDays())
                .build();
    }
}
