package com.plantNursery.GardeniaDreams.api.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
public record ApiErrorResponse(HttpStatus status, String message, LocalDateTime timestamp){
    public static ApiErrorResponse of(HttpStatus status, String message) {
        return ApiErrorResponse.builder()
                .status(status)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
