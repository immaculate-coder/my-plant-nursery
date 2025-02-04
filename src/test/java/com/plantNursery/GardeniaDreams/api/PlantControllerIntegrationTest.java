package com.plantNursery.GardeniaDreams.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantNursery.GardeniaDreams.api.request.CreatePlantApiRequest;
import com.plantNursery.GardeniaDreams.api.response.CreatePlantApiResponse;
import com.plantNursery.GardeniaDreams.core.PlantPersister;
import com.plantNursery.GardeniaDreams.core.model.CreatePlantRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}