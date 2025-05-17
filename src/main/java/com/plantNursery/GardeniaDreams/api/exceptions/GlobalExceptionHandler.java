package com.plantNursery.GardeniaDreams.api.exceptions;

import com.plantNursery.GardeniaDreams.api.response.ApiErrorResponse;
import com.plantNursery.GardeniaDreams.core.exceptions.PlantNotFoundException;
import com.plantNursery.GardeniaDreams.core.exceptions.WateringNotAllowedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PlantNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlePlantNotFoundException(PlantNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.of(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(WateringNotAllowedException.class)
    public ResponseEntity<ApiErrorResponse> handlePlantNotWateredException(WateringNotAllowedException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiErrorResponse.of(HttpStatus.CONFLICT, ex.getMessage()));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        logger.error("Exception occurred: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred."));
    }
}
