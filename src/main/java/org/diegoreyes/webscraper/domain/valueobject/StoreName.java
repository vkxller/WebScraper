package org.diegoreyes.webscraper.domain.valueobject;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;

public record StoreName(String value) {

    public StoreName {
        if (value == null || value.isBlank()) {
            throw new InvalidProductException("Store must not be blank");
        }
        value = value.trim();
    }

    public static StoreName of(String value) {
        return new StoreName(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
