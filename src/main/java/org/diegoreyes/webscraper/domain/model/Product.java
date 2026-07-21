package org.diegoreyes.webscraper.domain.model;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;

import java.math.BigDecimal;
import java.util.Objects;

public final class Product {

    private final String store;
    private final String name;
    private final BigDecimal price;
    private final BigDecimal previousPrice;
    private final String discount;
    private final String sourceUrl;

    public Product(
            String store,
            String name,
            BigDecimal price,
            BigDecimal previousPrice,
            String discount,
            String sourceUrl
    ) {
        this.store = validateRequiredText(
                store,
                "Store must not be blank"
        );

        this.name = validateRequiredText(
                name,
                "Product name must not be blank"
        );

        this.price = validatePrice(
                price,
                "Price must be greater than or equal to zero"
        );

        this.previousPrice = validatePreviousPrice(previousPrice);
        this.discount = normalizeOptionalText(discount);

        this.sourceUrl = validateRequiredText(
                sourceUrl,
                "Source URL must not be blank"
        );
    }

    private static String validateRequiredText(
            String value,
            String errorMessage
    ) {
        if (value == null || value.isBlank()) {
            throw new InvalidProductException(errorMessage);
        }

        return value.trim();
    }

    private static BigDecimal validatePrice(
            BigDecimal value,
            String errorMessage
    ) {
        if (value == null || value.signum() < 0) {
            throw new InvalidProductException(errorMessage);
        }

        return value;
    }

    private static BigDecimal validatePreviousPrice(
            BigDecimal previousPrice
    ) {
        if (previousPrice == null) {
            return null;
        }

        if (previousPrice.signum() < 0) {
            throw new InvalidProductException(
                    "Previous price must be greater than or equal to zero"
            );
        }

        return previousPrice;
    }

    private static String normalizeOptionalText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    public String getStore() {
        return store;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getPreviousPrice() {
        return previousPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Product product)) {
            return false;
        }

        return store.equals(product.store)
                && name.equals(product.name)
                && price.equals(product.price)
                && Objects.equals(previousPrice, product.previousPrice)
                && Objects.equals(discount, product.discount)
                && sourceUrl.equals(product.sourceUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                store,
                name,
                price,
                previousPrice,
                discount,
                sourceUrl
        );
    }

    @Override
    public String toString() {
        return "Product{" +
                "store='" + store + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", previousPrice=" + previousPrice +
                ", discount='" + discount + '\'' +
                ", sourceUrl='" + sourceUrl + '\'' +
                '}';
    }
}