package org.diegoreyes.webscraper.application.usecase;

import org.diegoreyes.webscraper.domain.exception.ProductNotFoundException;
import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetProductByIdUseCase unit tests")
class GetProductByIdUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    private GetProductByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetProductByIdUseCase(productRepository);
    }

    @Test
    @DisplayName("Should throw NullPointerException when repository is null")
    void shouldThrowExceptionWhenRepositoryIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> new GetProductByIdUseCase(null)
        );
    }

    @Test
    @DisplayName("Should throw NullPointerException when ProductId is null")
    void shouldThrowExceptionWhenProductIdIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> useCase.execute(null)
        );
    }

    @Test
    @DisplayName("Should return product when product exists in repository")
    void shouldReturnProductWhenFound() {
        ProductId id = ProductId.generate();
        Product product = new Product(id, "Falabella", "Notebook", new BigDecimal("500000"), null, null, null, null);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product result = useCase.execute(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Notebook", result.getName());
        verify(productRepository).findById(id);
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product does not exist")
    void shouldThrowNotFoundWhenProductMissing() {
        ProductId id = ProductId.generate();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> useCase.execute(id)
        );
        verify(productRepository).findById(id);
    }
}
