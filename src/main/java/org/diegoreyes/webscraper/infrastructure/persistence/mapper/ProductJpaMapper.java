package org.diegoreyes.webscraper.infrastructure.persistence.mapper;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;
import org.diegoreyes.webscraper.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.stereotype.Component;

@Component
public final class ProductJpaMapper {

    public ProductJpaEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductJpaEntity(
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

    public Product toDomain(ProductJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Product(
                ProductId.of(entity.getId()),
                entity.getStore(),
                entity.getName(),
                entity.getPrice(),
                entity.getPreviousPrice(),
                entity.getDiscount(),
                entity.getSourceUrl(),
                entity.getImageUrl()
        );
    }
}
