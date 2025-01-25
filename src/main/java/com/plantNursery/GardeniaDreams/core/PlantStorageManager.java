package com.plantNursery.GardeniaDreams.core;


import com.plantNursery.GardeniaDreams.utils.dto.PlantDTO;

public interface PlantStorageManager {
    String persist(PlantDTO plantDTO);
}
