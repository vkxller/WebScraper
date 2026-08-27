package org.diegoreyes.webscraper.application.usecase;

import org.diegoreyes.webscraper.domain.exception.ProductNotFoundException;
import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;

import java.util.Objects;

/**
 * Use case to retrieve a single product by its unique domain identifier.
 */
public class GetProductByIdUseCase {

    private final ProductRepository productRepository;

    public GetProductByIdUseCase(ProductRepository productRepository) {
        this.productRepository = Objects.requireNonNull(
                productRepository,
                "Product repository must not be null"
        );
    }

    public Product execute(ProductId id) {
        Objects.requireNonNull(id, "Product ID must not be null");
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product with ID " + id.value() + " was not found"
                ));
    }
}
