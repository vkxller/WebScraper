package org.diegoreyes.webscraper.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.diegoreyes.webscraper.domain.model.Product;

import java.math.BigDecimal;

@Schema(description = "Product representation returned by the REST API")
public record ProductResponseDto(
        @Schema(description = "Unique product identifier", example = "550e8400-e29b-41d4-a716-446655440000")
        String id,

        @Schema(description = "Store where product was found", example = "Falabella")
        String store,

        @Schema(description = "Name and title of the product", example = "Notebook Lenovo IdeaPad")
        String name,

        @Schema(description = "Current price in Chilean Pesos (CLP)", example = "699990.00")
        BigDecimal price,

        @Schema(description = "Previous original price before discount", example = "899990.00", nullable = true)
        BigDecimal previousPrice,

        @Schema(description = "Discount percentage tag", example = "-22%", nullable = true)
        String discount,

        @Schema(description = "Direct product source URL", example = "https://www.falabella.com/...", nullable = true)
        String sourceUrl,

        @Schema(description = "Product thumbnail image URL", example = "https://media.falabella.com/...", nullable = true)
        String imageUrl
) {
    public static ProductResponseDto fromDomain(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductResponseDto(
                product.getId().value(),
                product.getStore(),
                product.getName(),
                product.getPrice(),
                product.getPreviousPrice(),
                product.getDiscount(),
                product.getSourceUrl(),
                product.getImageUrl()
        );
    }
}
