package org.diegoreyes.webscraper.domain.valueobject;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;

public record Discount(String value) {

    public Discount {
        if (value == null || value.isBlank()) {
            throw new InvalidProductException("Discount must not be blank");
        }
        value = value.trim();
    }

    public static Discount of(String value) {
        return value == null || value.isBlank() ? null : new Discount(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
