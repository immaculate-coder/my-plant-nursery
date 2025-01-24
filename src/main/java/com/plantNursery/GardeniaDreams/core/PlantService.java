package com.plantNursery.GardeniaDreams.core;

import com.plantNursery.GardeniaDreams.api.request.PlantApiRequest;
import com.plantNursery.GardeniaDreams.utils.dto.PlantDTO;

public interface PlantService {
    void persist(PlantApiRequest plantApiRequest);
}
