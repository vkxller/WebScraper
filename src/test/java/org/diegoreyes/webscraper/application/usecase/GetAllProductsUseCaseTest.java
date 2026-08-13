package org.diegoreyes.webscraper.application.usecase;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllProductsUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @Test
    void shouldReturnAllProductsFromRepository() {
        Product product = new Product(
                ProductId.of("prod-1"),
                "Falabella",
                "Notebook Lenovo",
                new BigDecimal("499990"),
                null,
                null,
                null,
                null
        );
        List<Product> products = List.of(product);

        when(productRepository.findAll()).thenReturn(products);

        GetAllProductsUseCase useCase = new GetAllProductsUseCase(productRepository);
        List<Product> result = useCase.execute();

        assertEquals(products, result);
        verify(productRepository).findAll();
    }

    @Test
    void shouldRejectNullProductRepository() {
        assertThrows(
                NullPointerException.class,
                () -> new GetAllProductsUseCase(null)
        );
    }
}
