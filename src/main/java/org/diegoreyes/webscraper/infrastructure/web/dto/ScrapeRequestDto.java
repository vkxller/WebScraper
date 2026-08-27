package org.diegoreyes.webscraper.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body to trigger a scraping operation by search query or URL")
public record ScrapeRequestDto(
        @Schema(description = "Search query keyword to scrape from Falabella", example = "zapatillas", nullable = true)
        @Size(max = 200, message = "Search query cannot exceed 200 characters")
        String query,

        @Schema(description = "Specific target URL to scrape directly", example = "https://www.falabella.com/falabella-cl/search?Ntt=zapatillas", nullable = true)
        @Size(max = 1000, message = "Target URL cannot exceed 1000 characters")
        String targetUrl
) {
}
