package org.diegoreyes.webscraper.domain.repository;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    void save(Product product);

    void saveAll(List<Product> products);

    List<Product> findAll();

    Optional<Product> findById(ProductId id);

    boolean existsById(ProductId id);

    void deleteById(ProductId id);

    void clear();

    int count();
}
