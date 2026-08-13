package org.diegoreyes.webscraper.domain.valueobject;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;

import java.util.UUID;

public record ProductId(String value) {

    public ProductId {
        if (value == null || value.isBlank()) {
            throw new InvalidProductException("Product ID must not be blank");
        }
        value = value.trim();
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID().toString());
    }

    public static ProductId of(String value) {
        return new ProductId(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
