package org.diegoreyes.webscraper.domain.valueobject;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;

import java.math.BigDecimal;

public record Price(BigDecimal amount) {

    public Price {
        if (amount == null || amount.signum() < 0) {
            throw new InvalidProductException("Price must be greater than or equal to zero");
        }
    }

    public static Price of(BigDecimal amount) {
        return amount == null ? null : new Price(amount);
    }

    public static Price of(String amount) {
        if (amount == null || amount.isBlank()) {
            throw new InvalidProductException("Price must be greater than or equal to zero");
        }
        try {
            return new Price(new BigDecimal(amount));
        } catch (NumberFormatException exception) {
            throw new InvalidProductException("Price format is invalid");
        }
    }

    public static Price zero() {
        return new Price(BigDecimal.ZERO);
    }

    @Override
    public String toString() {
        return amount.toString();
    }
}
