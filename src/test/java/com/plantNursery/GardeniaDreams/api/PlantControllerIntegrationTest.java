package com.plantNursery.GardeniaDreams.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantNursery.GardeniaDreams.api.request.CreatePlantApiRequest;
import com.plantNursery.GardeniaDreams.api.response.ApiErrorResponse;
import com.plantNursery.GardeniaDreams.api.response.CreatePlantApiResponse;
import com.plantNursery.GardeniaDreams.api.response.GetAllPlantsApiResponse;
import com.plantNursery.GardeniaDreams.api.response.PlantApiResponse;
import com.plantNursery.GardeniaDreams.core.PlantFetcher;
import com.plantNursery.GardeniaDreams.core.PlantPersister;
import com.plantNursery.GardeniaDreams.core.exceptions.PlantNotFoundException;
import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import com.plantNursery.GardeniaDreams.core.model.Plant;
import com.plantNursery.GardeniaDreams.core.model.UpdatePlantRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PlantController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PlantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PlantPersister plantPersister;

    @MockitoBean
    private PlantFetcher plantFetcher;

    @Test
    void createNewPlant_shouldReturnId() throws Exception {
        Date lastWateredDate = new Date();
        CreatePlantApiRequest apiRequest = getApiRequest(lastWateredDate);
        String mockedPlantId = "dummy-id-123";
        CreatePlantRequest request = getRequest(lastWateredDate);

        Mockito.when(plantPersister.persist(request)).thenReturn(mockedPlantId);

        CreatePlantApiResponse expectedResponse = getExpectedResponse(mockedPlantId);

        mockMvc.perform(post("/api/v1/plants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apiRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedResponse.id()))
                .andExpect(jsonPath("$.message").value(expectedResponse.message()));
    }

    @Test
    void getAllPlants_shouldReturnPlantList() throws Exception {
        List<Plant> mockedPlants = getMockedPlantList();
        Mockito.when(plantFetcher.getAllPlants()).thenReturn(mockedPlants);

        List<PlantApiResponse> expectedResponses = mockedPlants.stream()
                .map(this::toPlantApiResponse)
                .toList();

        GetAllPlantsApiResponse expectedApiResponse = GetAllPlantsApiResponse.builder()
                .plantsCount(expectedResponses.size())
                .plants(expectedResponses)
                .build();

        mockMvc.perform(get("/api/v1/plants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.plantsCount").value(expectedResponses.size()))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedApiResponse)));
    }

    @Test
    void getPlantById_shouldReturnPlant() throws Exception {
        String plantId = "test-id-1";
        Plant mockedPlant = getMockedPlantList().getFirst();
        Mockito.when(plantFetcher.getPlantById(plantId)).thenReturn(mockedPlant);

        PlantApiResponse expectedResponse = toPlantApiResponse(mockedPlant);

        mockMvc.perform(get("/api/v1/plants/{id}", plantId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedResponse.id()))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void getPlantById_shouldReturnNotFound() throws Exception {
        String nonExistentPlantId = "invalid-id";
        String errorMessage = "Plant not found with id : " + nonExistentPlantId;
        Mockito.when(plantFetcher.getPlantById(nonExistentPlantId))
                .thenThrow(new PlantNotFoundException(errorMessage));

        ApiErrorResponse errorResponse = ApiErrorResponse.of(HttpStatus.NOT_FOUND, nonExistentPlantId);

        mockMvc.perform(get("/api/v1/plants/{id}", nonExistentPlantId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    void deletePlantById_shouldReturnSuccess() throws Exception {
        String plantId = "test-id-1";
        Mockito.when(plantPersister.deletePlant(plantId)).thenReturn(plantId);
        mockMvc.perform(delete("/api/v1/plants/{id}", plantId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Plant deleted successfully with id : " + plantId));
    }

    @Test
    void deletePlantById_shouldReturnNotFound() throws Exception {
        String nonExistentPlantId = "invalid-id";
        String errorMessage = "Plant not found with id : " + nonExistentPlantId;

        doThrow(new PlantNotFoundException("Plant not found with id : " + nonExistentPlantId))
                .when(plantPersister).deletePlant(nonExistentPlantId);

        mockMvc.perform(delete("/api/v1/plants/{id}", nonExistentPlantId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    void updatePlantById_shouldReturnNotFound() throws Exception {
        String nonExistentPlantId = "invalid-id";
        String errorMessage = "Plant not found with id : " + nonExistentPlantId;

        doThrow(new PlantNotFoundException("Plant not found with id : " + nonExistentPlantId))
                .when(plantPersister).updatePlant(anyString(), any(UpdatePlantRequest.class));

        mockMvc.perform(patch("/api/v1/plants/{id}", nonExistentPlantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getUpdatePlantRequest())))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    void updatePlantById_shouldReturnPlantApiResponse() throws Exception {
        String plantId = "test-id-1";
        Plant mockedPlant = getMockedPlantList().getFirst();
        Mockito.when(plantPersister.updatePlant(anyString(), any(UpdatePlantRequest.class))).thenReturn(mockedPlant);

        PlantApiResponse expectedResponse = toPlantApiResponse(mockedPlant);

        mockMvc.perform(patch("/api/v1/plants/{id}", plantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getUpdatePlantRequest())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedResponse.id()))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    private static UpdatePlantRequest getUpdatePlantRequest() {
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        String plantName = "Test Updated Plant";
        int ageInDays = 200;
        int wateringIntervalInDays = 10;

        return UpdatePlantRequest.builder()
                .name(plantName)
                .ageInDays(ageInDays)
                .lastWateredDate(yesterday)
                .wateringIntervalInDays(wateringIntervalInDays)
                .build();
    }


    private static CreatePlantApiResponse getExpectedResponse(String mockedPlantId) {
        return CreatePlantApiResponse.builder()
                .id(mockedPlantId)
                .message("Plant saved successfully")
                .build();
    }

    private static CreatePlantRequest getRequest(Date lastWateredDate) {
        return CreatePlantRequest.builder()
                .name("Test tree")
                .ageInDays(200)
                .lastWateredDate(lastWateredDate)
                .wateringIntervalInDays(10)
                .build();
    }

    private static CreatePlantApiRequest getApiRequest(Date lastWateredDate) {
        return CreatePlantApiRequest.builder()
                .name("Test tree")
                .ageInDays(200)
                .lastWateredDate(lastWateredDate)
                .wateringIntervalInDays(10)
                .build();
    }

    private static List<Plant> getMockedPlantList() {
        Date today = new Date();
        return Arrays.asList(
                Plant.builder()
                        .id("test-id-1")
                        .name("Rose")
                        .ageInDays(120)
                        .wateringIntervalInDays(7)
                        .lastWateredDate(today)
                        .build(),
                Plant.builder()
                        .id("test-id-2")
                        .name("Tulip")
                        .ageInDays(90)
                        .wateringIntervalInDays(5)
                        .lastWateredDate(today)
                        .build()
        );
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