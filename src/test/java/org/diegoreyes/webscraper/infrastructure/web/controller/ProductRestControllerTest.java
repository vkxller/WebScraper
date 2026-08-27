package org.diegoreyes.webscraper.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.diegoreyes.webscraper.application.usecase.DeleteProductByIdUseCase;
import org.diegoreyes.webscraper.application.usecase.GetAllProductsUseCase;
import org.diegoreyes.webscraper.application.usecase.GetProductByIdUseCase;
import org.diegoreyes.webscraper.application.usecase.ScrapeAndSaveProductsUseCase;
import org.diegoreyes.webscraper.domain.exception.InvalidProductException;
import org.diegoreyes.webscraper.domain.exception.ProductNotFoundException;
import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;
import org.diegoreyes.webscraper.infrastructure.web.dto.ScrapeRequestDto;
import org.diegoreyes.webscraper.infrastructure.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductRestController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("ProductRestController MockMvc tests")
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GetAllProductsUseCase getAllProductsUseCase;

    @MockBean
    private GetProductByIdUseCase getProductByIdUseCase;

    @MockBean
    private DeleteProductByIdUseCase deleteProductByIdUseCase;

    @MockBean
    private ScrapeAndSaveProductsUseCase scrapeAndSaveProductsUseCase;

    @MockBean
    private ProductRepository productRepository;

    @Test
    @DisplayName("GET /api/products should return 200 with list of products")
    void shouldReturnAllProducts() throws Exception {
        ProductId id = ProductId.generate();
        Product product = new Product(id, "Falabella", "Notebook HP", new BigDecimal("499990.00"), null, null, null, null);

        when(getAllProductsUseCase.execute()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(id.value()))
                .andExpect(jsonPath("$[0].store").value("Falabella"))
                .andExpect(jsonPath("$[0].name").value("Notebook HP"))
                .andExpect(jsonPath("$[0].price").value(499990.00));
    }

    @Test
    @DisplayName("GET /api/products?search=zapatillas should scrape search query")
    void shouldScrapeOnSearchParam() throws Exception {
        ProductId id = ProductId.generate();
        Product product = new Product(id, "Falabella", "Zapatilla Nike", new BigDecimal("69990.00"), null, null, null, null);

        when(scrapeAndSaveProductsUseCase.execute(any(URI.class))).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products").param("search", "zapatillas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Zapatilla Nike"));

        verify(scrapeAndSaveProductsUseCase).execute(any(URI.class));
    }

    @Test
    @DisplayName("GET /api/products/{id} should return 200 when product exists")
    void shouldReturnProductById() throws Exception {
        ProductId id = ProductId.generate();
        Product product = new Product(id, "Falabella", "Tablet", new BigDecimal("129990.00"), null, null, null, null);

        when(getProductByIdUseCase.execute(id)).thenReturn(product);

        mockMvc.perform(get("/api/products/" + id.value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.value()))
                .andExpect(jsonPath("$.name").value("Tablet"));
    }

    @Test
    @DisplayName("GET /api/products/{id} should return 404 when product not found")
    void shouldReturn404WhenProductNotFound() throws Exception {
        ProductId id = ProductId.generate();

        when(getProductByIdUseCase.execute(id))
                .thenThrow(new ProductNotFoundException("Product with ID " + id.value() + " was not found"));

        mockMvc.perform(get("/api/products/" + id.value()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Product with ID " + id.value() + " was not found"))
                .andExpect(jsonPath("$.path").value("/api/products/" + id.value()));
    }

    @Test
    @DisplayName("GET /api/products/{id} should return 400 when product id is blank")
    void shouldReturn400WhenProductIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/products/   "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("POST /api/products/scrape should return 201 with scraped products")
    void shouldScrapeProductsViaPost() throws Exception {
        ProductId id = ProductId.generate();
        Product product = new Product(id, "Falabella", "Smart TV", new BigDecimal("399990.00"), null, null, null, null);

        when(scrapeAndSaveProductsUseCase.execute(any(URI.class))).thenReturn(List.of(product));

        ScrapeRequestDto requestDto = new ScrapeRequestDto("televisores", null);

        mockMvc.perform(post("/api/products/scrape")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].name").value("Smart TV"));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} should return 204 when deleted")
    void shouldDeleteProductById() throws Exception {
        ProductId id = ProductId.generate();

        mockMvc.perform(delete("/api/products/" + id.value()))
                .andExpect(status().isNoContent());

        verify(deleteProductByIdUseCase).execute(id);
    }

    @Test
    @DisplayName("DELETE /api/products should return 204 on clear")
    void shouldClearAllProducts() throws Exception {
        mockMvc.perform(delete("/api/products"))
                .andExpect(status().isNoContent());

        verify(productRepository).clear();
    }
}
