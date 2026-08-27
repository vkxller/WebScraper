package org.diegoreyes.webscraper.infrastructure.persistence;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;
import org.diegoreyes.webscraper.infrastructure.persistence.entity.ProductJpaEntity;
import org.diegoreyes.webscraper.infrastructure.persistence.mapper.ProductJpaMapper;
import org.diegoreyes.webscraper.infrastructure.persistence.repository.SpringDataProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostgreSqlProductRepository adapter unit tests")
class PostgreSqlProductRepositoryTest {

    @Mock
    private SpringDataProductRepository springDataRepository;

    private final ProductJpaMapper mapper = new ProductJpaMapper();

    private PostgreSqlProductRepository repository;

    @BeforeEach
    void setUp() {
        repository = new PostgreSqlProductRepository(springDataRepository, mapper);
    }

    @Test
    @DisplayName("Constructor should throw NPE when dependencies are null")
    void constructorShouldValidateArguments() {
        assertThrows(NullPointerException.class, () -> new PostgreSqlProductRepository(null, mapper));
        assertThrows(NullPointerException.class, () -> new PostgreSqlProductRepository(springDataRepository, null));
    }

    @Test
    @DisplayName("Should save a product through Spring Data JPA")
    void shouldSaveProduct() {
        ProductId id = ProductId.generate();
        Product product = new Product(id, "Falabella", "Notebook", new BigDecimal("500000.00"), null, null, null, null);

        assertDoesNotThrow(() -> repository.save(product));
        verify(springDataRepository).save(any(ProductJpaEntity.class));
    }

    @Test
    @DisplayName("Should save multiple products")
    void shouldSaveAllProducts() {
        ProductId id1 = ProductId.generate();
        ProductId id2 = ProductId.generate();
        Product p1 = new Product(id1, "Falabella", "Item 1", new BigDecimal("10000.00"), null, null, null, null);
        Product p2 = new Product(id2, "Falabella", "Item 2", new BigDecimal("20000.00"), null, null, null, null);

        assertDoesNotThrow(() -> repository.saveAll(List.of(p1, p2)));
        verify(springDataRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Should find product by ID when present")
    void shouldFindByIdWhenPresent() {
        ProductId id = ProductId.generate();
        Product product = new Product(id, "Falabella", "Televisor", new BigDecimal("300000.00"), null, null, null, null);
        ProductJpaEntity entity = mapper.toEntity(product);

        when(springDataRepository.findById(id.value())).thenReturn(Optional.of(entity));

        Optional<Product> result = repository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    @DisplayName("Should return empty optional when product not found")
    void shouldReturnEmptyWhenNotFound() {
        ProductId id = ProductId.generate();

        when(springDataRepository.findById(id.value())).thenReturn(Optional.empty());

        Optional<Product> result = repository.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should find all products")
    void shouldFindAllProducts() {
        ProductId id = ProductId.generate();
        Product product = new Product(id, "Falabella", "Celular", new BigDecimal("200000.00"), null, null, null, null);

        when(springDataRepository.findAll()).thenReturn(List.of(mapper.toEntity(product)));

        List<Product> all = repository.findAll();

        assertEquals(1, all.size());
        assertEquals(id, all.get(0).getId());
    }

    @Test
    @DisplayName("Should check existence by ID")
    void shouldCheckExistsById() {
        ProductId id = ProductId.generate();

        when(springDataRepository.existsById(id.value())).thenReturn(true);

        assertTrue(repository.existsById(id));
    }

    @Test
    @DisplayName("Should delete by ID")
    void shouldDeleteById() {
        ProductId id = ProductId.generate();

        repository.deleteById(id);

        verify(springDataRepository).deleteById(id.value());
    }

    @Test
    @DisplayName("Should clear all products")
    void shouldClearAll() {
        repository.clear();

        verify(springDataRepository).deleteAll();
    }

    @Test
    @DisplayName("Should count products")
    void shouldCountProducts() {
        when(springDataRepository.count()).thenReturn(5L);

        assertEquals(5, repository.count());
    }
}
