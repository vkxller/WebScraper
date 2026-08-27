package org.diegoreyes.webscraper.application.usecase;

import org.diegoreyes.webscraper.domain.exception.ProductNotFoundException;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;

import java.util.Objects;

/**
 * Use case to delete a product by its unique domain identifier.
 */
public class DeleteProductByIdUseCase {

    private final ProductRepository productRepository;

    public DeleteProductByIdUseCase(ProductRepository productRepository) {
        this.productRepository = Objects.requireNonNull(
                productRepository,
                "Product repository must not be null"
        );
    }

    public void execute(ProductId id) {
        Objects.requireNonNull(id, "Product ID must not be null");
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(
                    "Cannot delete: Product with ID " + id.value() + " does not exist"
            );
        }
        productRepository.deleteById(id);
    }
}
