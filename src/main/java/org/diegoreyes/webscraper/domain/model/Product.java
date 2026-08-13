package org.diegoreyes.webscraper.domain.model;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;
import org.diegoreyes.webscraper.domain.valueobject.Discount;
import org.diegoreyes.webscraper.domain.valueobject.Price;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;
import org.diegoreyes.webscraper.domain.valueobject.ProductName;
import org.diegoreyes.webscraper.domain.valueobject.ProductUrl;
import org.diegoreyes.webscraper.domain.valueobject.StoreName;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Product Entity (Aggregate Root) representing an e-commerce product.
 * Follows DDD Tactical Patterns: unique identity (ProductId), lifecycle,
 * and internal encapsulation using self-validating Value Objects.
 */
public final class Product {

    private final ProductId id;
    private final StoreName store;
    private final ProductName name;
    private final Price price;
    private final Price previousPrice;
    private final Discount discount;
    private final ProductUrl sourceUrl;
    private final ProductUrl imageUrl;

    public Product(
            ProductId id,
            StoreName store,
            ProductName name,
            Price price,
            Price previousPrice,
            Discount discount,
            ProductUrl sourceUrl,
            ProductUrl imageUrl
    ) {
        this.id = Objects.requireNonNull(id, "Product ID must not be null");
        this.store = Objects.requireNonNull(store, "Store must not be null");
        this.name = Objects.requireNonNull(name, "Product name must not be null");
        this.price = Objects.requireNonNull(price, "Price must not be null");
        this.previousPrice = previousPrice;
        this.discount = discount;
        this.sourceUrl = sourceUrl;
        this.imageUrl = imageUrl;
    }

    public Product(
            ProductId id,
            String store,
            String name,
            BigDecimal price,
            BigDecimal previousPrice,
            String discount,
            String sourceUrl,
            String imageUrl
    ) {
        this(
                Objects.requireNonNull(id, "Product ID must not be null"),
                new StoreName(store),
                new ProductName(name),
                new Price(price),
                validatePreviousPrice(previousPrice),
                Discount.of(discount),
                ProductUrl.of(sourceUrl),
                ProductUrl.of(imageUrl)
        );
    }

    public Product(
            String store,
            String name,
            BigDecimal price,
            BigDecimal previousPrice,
            String discount,
            String sourceUrl,
            String imageUrl
    ) {
        this(
                ProductId.generate(),
                store,
                name,
                price,
                previousPrice,
                discount,
                sourceUrl,
                imageUrl
        );
    }

    public Product(
            String store,
            String name,
            BigDecimal price,
            BigDecimal previousPrice,
            String discount,
            String sourceUrl
    ) {
        this(
                store,
                name,
                price,
                previousPrice,
                discount,
                sourceUrl,
                null
        );
    }

    private static Price validatePreviousPrice(BigDecimal previousPrice) {
        if (previousPrice == null) {
            return null;
        }
        if (previousPrice.signum() < 0) {
            throw new InvalidProductException("Previous price must be greater than or equal to zero");
        }
        return new Price(previousPrice);
    }

    public ProductId getId() {
        return id;
    }

    public StoreName getStoreName() {
        return store;
    }

    public ProductName getProductName() {
        return name;
    }

    public Price getProductPrice() {
        return price;
    }

    public Price getProductPreviousPrice() {
        return previousPrice;
    }

    public Discount getProductDiscount() {
        return discount;
    }

    public ProductUrl getProductSourceUrl() {
        return sourceUrl;
    }

    public ProductUrl getProductImageUrl() {
        return imageUrl;
    }

    public String getStore() {
        return store.value();
    }

    public String getName() {
        return name.value();
    }

    public BigDecimal getPrice() {
        return price.amount();
    }

    public BigDecimal getPreviousPrice() {
        return previousPrice != null ? previousPrice.amount() : null;
    }

    public String getDiscount() {
        return discount != null ? discount.value() : null;
    }

    public String getSourceUrl() {
        return sourceUrl != null ? sourceUrl.value() : null;
    }

    public String getImageUrl() {
        return imageUrl != null ? imageUrl.value() : null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Product other)) {
            return false;
        }

        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", store='" + getStore() + '\'' +
                ", name='" + getName() + '\'' +
                ", price=" + getPrice() +
                ", previousPrice=" + getPreviousPrice() +
                ", discount='" + getDiscount() + '\'' +
                ", sourceUrl='" + getSourceUrl() + '\'' +
                ", imageUrl='" + getImageUrl() + '\'' +
                '}';
    }
}