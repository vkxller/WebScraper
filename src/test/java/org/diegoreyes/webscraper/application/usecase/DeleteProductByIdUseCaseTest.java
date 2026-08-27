package org.diegoreyes.webscraper.application.usecase;

import org.diegoreyes.webscraper.domain.exception.ProductNotFoundException;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteProductByIdUseCase unit tests")
class DeleteProductByIdUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    private DeleteProductByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteProductByIdUseCase(productRepository);
    }

    @Test
    @DisplayName("Should throw NullPointerException when repository is null")
    void shouldThrowExceptionWhenRepositoryIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> new DeleteProductByIdUseCase(null)
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
    @DisplayName("Should delete product when product exists")
    void shouldDeleteProductWhenExists() {
        ProductId id = ProductId.generate();

        when(productRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> useCase.execute(id));
        verify(productRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product does not exist")
    void shouldThrowNotFoundWhenProductMissing() {
        ProductId id = ProductId.generate();

        when(productRepository.existsById(id)).thenReturn(false);

        assertThrows(
                ProductNotFoundException.class,
                () -> useCase.execute(id)
        );
        verify(productRepository, never()).deleteById(any());
    }
}
