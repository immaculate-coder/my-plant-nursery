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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PlantController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PlantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PlantPersister plantPersister;

    @Test
    void createNewPlant_shouldReturnId() throws Exception {
        CreatePlantApiRequest request = CreatePlantApiRequest.builder()
                .name("Test tree")
                .ageInDays(200)
                .lastWateredDate(new Date())
                .wateringIntervalInDays(10)
                .build();

        String mockedPlantId = "dummy-id-123";
        Mockito.when(plantPersister.persist(any(CreatePlantRequest.class))).thenReturn(mockedPlantId);

        CreatePlantApiResponse expectedResponse = CreatePlantApiResponse.builder()
                .id(mockedPlantId)
                .message("Plant saved successfully")
                .build();

        mockMvc.perform(post("/api/v1/plants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedResponse.id()))
                .andExpect(jsonPath("$.message").value(expectedResponse.message()));
    }
}
