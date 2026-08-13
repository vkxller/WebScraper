package org.diegoreyes.webscraper.domain.valueobject;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;

public record ProductUrl(String value) {

    public ProductUrl {
        if (value == null || value.isBlank()) {
            throw new InvalidProductException("Product URL must not be blank");
        }
        value = value.trim();
    }

    public static ProductUrl of(String value) {
        return value == null || value.isBlank() ? null : new ProductUrl(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
