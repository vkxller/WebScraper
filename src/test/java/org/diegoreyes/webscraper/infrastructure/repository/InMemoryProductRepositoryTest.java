package org.diegoreyes.webscraper.infrastructure.repository;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryProductRepositoryTest {

    private InMemoryProductRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryProductRepository();
    }

    @Test
    void shouldSaveAndFindProductById() {
        ProductId id = ProductId.of("prod-1");
        Product product = createProduct(id, "Notebook Lenovo");

        repository.save(product);

        Optional<Product> found = repository.findById(id);
        assertTrue(found.isPresent());
        assertEquals(product, found.get());
        assertTrue(repository.existsById(id));
        assertEquals(1, repository.count());
    }

    @Test
    void shouldReturnEmptyOptionalWhenNotFound() {
        Optional<Product> found = repository.findById(ProductId.of("non-existent"));
        assertTrue(found.isEmpty());
        assertFalse(repository.existsById(ProductId.of("non-existent")));
    }

    @Test
    void shouldSaveAllAndFindAllProducts() {
        Product p1 = createProduct(ProductId.of("prod-1"), "Notebook Lenovo");
        Product p2 = createProduct(ProductId.of("prod-2"), "Notebook HP");

        repository.saveAll(List.of(p1, p2));

        List<Product> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(p1));
        assertTrue(all.contains(p2));
        assertEquals(2, repository.count());
    }

    @Test
    void shouldReturnImmutableListFromFindAll() {
        Product p1 = createProduct(ProductId.of("prod-1"), "Notebook Lenovo");
        repository.save(p1);

        List<Product> all = repository.findAll();
        assertThrows(
                UnsupportedOperationException.class,
                () -> all.add(createProduct(ProductId.of("prod-2"), "Notebook HP"))
        );
    }

    @Test
    void shouldDeleteProductById() {
        ProductId id = ProductId.of("prod-1");
        Product product = createProduct(id, "Notebook Lenovo");

        repository.save(product);
        assertEquals(1, repository.count());

        repository.deleteById(id);
        assertFalse(repository.existsById(id));
        assertEquals(0, repository.count());
    }

    @Test
    void shouldClearAllProducts() {
        Product p1 = createProduct(ProductId.of("prod-1"), "Notebook Lenovo");
        Product p2 = createProduct(ProductId.of("prod-2"), "Notebook HP");

        repository.saveAll(List.of(p1, p2));
        assertEquals(2, repository.count());

        repository.clear();
        assertEquals(0, repository.count());
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldRejectNullProductOnSave() {
        assertThrows(
                NullPointerException.class,
                () -> repository.save(null)
        );
    }

    @Test
    void shouldRejectNullListOnSaveAll() {
        assertThrows(
                NullPointerException.class,
                () -> repository.saveAll(null)
        );
    }

    @Test
    void shouldRejectNullIdOnFindById() {
        assertThrows(
                NullPointerException.class,
                () -> repository.findById(null)
        );
    }

    @Test
    void shouldRejectNullIdOnExistsById() {
        assertThrows(
                NullPointerException.class,
                () -> repository.existsById(null)
        );
    }

    @Test
    void shouldRejectNullIdOnDeleteById() {
        assertThrows(
                NullPointerException.class,
                () -> repository.deleteById(null)
        );
    }

    private Product createProduct(ProductId id, String name) {
        return new Product(
                id,
                "Falabella",
                name,
                new BigDecimal("499990"),
                new BigDecimal("599990"),
                "-17%",
                "https://falabella.com/product/1",
                "https://falabella.com/image/1.jpg"
        );
    }
}
