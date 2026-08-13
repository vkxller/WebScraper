package org.diegoreyes.webscraper.application.usecase;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;

import java.util.List;
import java.util.Objects;

/**
 * Use case to retrieve all persisted products from the domain repository.
 */
public final class GetAllProductsUseCase {

    private final ProductRepository productRepository;

    public GetAllProductsUseCase(ProductRepository productRepository) {
        this.productRepository = Objects.requireNonNull(
                productRepository,
                "ProductRepository must not be null"
        );
    }

    public List<Product> execute() {
        return productRepository.findAll();
    }
}
