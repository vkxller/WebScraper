package org.diegoreyes.webscraper.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Unified error response structure returned on HTTP failures")
public record ErrorResponseDto(
        @Schema(description = "Timestamp when the error occurred", example = "2026-08-16T23:45:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,

        @Schema(description = "HTTP status code number", example = "400")
        int status,

        @Schema(description = "HTTP error title", example = "Bad Request")
        String error,

        @Schema(description = "Descriptive error message", example = "Invalid product name: cannot be blank")
        String message,

        @Schema(description = "Request path that caused the error", example = "/api/products")
        String path
) {
    public static ErrorResponseDto of(int status, String error, String message, String path) {
        return new ErrorResponseDto(
                LocalDateTime.now(),
                status,
                error,
                message,
                path
        );
    }
}
