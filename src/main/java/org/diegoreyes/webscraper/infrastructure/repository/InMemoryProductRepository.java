package org.diegoreyes.webscraper.infrastructure.repository;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory thread-safe implementation of ProductRepository.
 */
public final class InMemoryProductRepository implements ProductRepository {

    private final Map<ProductId, Product> storage =
            new ConcurrentHashMap<>();

    @Override
    public void save(Product product) {
        Objects.requireNonNull(
                product,
                "Product must not be null"
        );
        storage.put(product.getId(), product);
    }

    @Override
    public void saveAll(List<Product> products) {
        Objects.requireNonNull(
                products,
                "Products list must not be null"
        );
        for (Product product : products) {
            save(product);
        }
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(
                new ArrayList<>(storage.values())
        );
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        Objects.requireNonNull(
                id,
                "Product ID must not be null"
        );
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public boolean existsById(ProductId id) {
        Objects.requireNonNull(
                id,
                "Product ID must not be null"
        );
        return storage.containsKey(id);
    }

    @Override
    public void deleteById(ProductId id) {
        Objects.requireNonNull(
                id,
                "Product ID must not be null"
        );
        storage.remove(id);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int count() {
        return storage.size();
    }
}
