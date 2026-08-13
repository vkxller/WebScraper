package org.diegoreyes.webscraper.domain.valueobject;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;

public record ProductName(String value) {

    public ProductName {
        if (value == null || value.isBlank()) {
            throw new InvalidProductException("Product name must not be blank");
        }
        value = value.trim();
    }

    public static ProductName of(String value) {
        return new ProductName(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
