package org.diegoreyes.webscraper.infrastructure.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.diegoreyes.webscraper.application.usecase.DeleteProductByIdUseCase;
import org.diegoreyes.webscraper.application.usecase.GetAllProductsUseCase;
import org.diegoreyes.webscraper.application.usecase.GetProductByIdUseCase;
import org.diegoreyes.webscraper.application.usecase.ScrapeAndSaveProductsUseCase;
import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;
import org.diegoreyes.webscraper.infrastructure.web.dto.ErrorResponseDto;
import org.diegoreyes.webscraper.infrastructure.web.dto.ProductResponseDto;
import org.diegoreyes.webscraper.infrastructure.web.dto.ScrapeRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS})
@Tag(name = "Products", description = "REST API for e-commerce product scraping, persistence and retrieval")
public class ProductRestController {

    private static final URI DEFAULT_SCRAPE_URI = URI.create(
            "https://www.falabella.com/falabella-cl/category/cat40052/Computadores"
    );

    private static final String FALABELLA_SEARCH_BASE =
            "https://www.falabella.com/falabella-cl/search?Ntt=";

    private final GetAllProductsUseCase getAllProductsUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final DeleteProductByIdUseCase deleteProductByIdUseCase;
    private final ScrapeAndSaveProductsUseCase scrapeAndSaveProductsUseCase;
    private final ProductRepository productRepository;

    public ProductRestController(
            GetAllProductsUseCase getAllProductsUseCase,
            GetProductByIdUseCase getProductByIdUseCase,
            DeleteProductByIdUseCase deleteProductByIdUseCase,
            ScrapeAndSaveProductsUseCase scrapeAndSaveProductsUseCase,
            ProductRepository productRepository
    ) {
        this.getAllProductsUseCase = Objects.requireNonNull(getAllProductsUseCase);
        this.getProductByIdUseCase = Objects.requireNonNull(getProductByIdUseCase);
        this.deleteProductByIdUseCase = Objects.requireNonNull(deleteProductByIdUseCase);
        this.scrapeAndSaveProductsUseCase = Objects.requireNonNull(scrapeAndSaveProductsUseCase);
        this.productRepository = Objects.requireNonNull(productRepository);
    }

    @GetMapping
    @Operation(summary = "Get all products or search live in Falabella", description = "Returns persisted products or triggers a live search if query parameter is present.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<List<ProductResponseDto>> getProducts(
            @Parameter(description = "Optional search query to scrape live from Falabella Chile")
            @RequestParam(name = "search", required = false) String search
    ) throws IOException {

        if (search != null && !search.trim().isBlank()) {
            URI searchUri = resolveSearchUri(search.trim());
            List<Product> scrapedProducts = scrapeAndSaveProductsUseCase.execute(searchUri);
            return ResponseEntity.ok(scrapedProducts.stream().map(ProductResponseDto::fromDomain).toList());
        }

        List<Product> persisted = getAllProductsUseCase.execute();
        if (persisted.isEmpty()) {
            // First time load: auto-scrape default catalog
            persisted = scrapeAndSaveProductsUseCase.execute(DEFAULT_SCRAPE_URI);
        }

        return ResponseEntity.ok(persisted.stream().map(ProductResponseDto::fromDomain).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by unique ID", description = "Retrieves a single product from database by its ProductId UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid product ID",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<ProductResponseDto> getProductById(
            @Parameter(description = "Product UUID identifier", required = true)
            @PathVariable("id") String id
    ) {
        ProductId productId = ProductId.of(id);
        Product product = getProductByIdUseCase.execute(productId);
        return ResponseEntity.ok(ProductResponseDto.fromDomain(product));
    }

    @PostMapping("/scrape")
    @Operation(summary = "Trigger a product scraping operation", description = "Scrapes products for the specified keyword or URL and stores them in PostgreSQL.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Scraping completed and products persisted",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid payload or URL format",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "502", description = "Store connectivity error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<List<ProductResponseDto>> scrapeProducts(
            @Valid @RequestBody(required = false) ScrapeRequestDto requestDto
    ) throws IOException {

        URI targetUri;
        if (requestDto != null && requestDto.targetUrl() != null && !requestDto.targetUrl().isBlank()) {
            targetUri = URI.create(requestDto.targetUrl().trim());
        } else if (requestDto != null && requestDto.query() != null && !requestDto.query().isBlank()) {
            targetUri = resolveSearchUri(requestDto.query().trim());
        } else {
            targetUri = DEFAULT_SCRAPE_URI;
        }

        List<Product> scrapedProducts = scrapeAndSaveProductsUseCase.execute(targetUri);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(scrapedProducts.stream().map(ProductResponseDto::fromDomain).toList());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product by ID", description = "Removes a product from the database by its ProductId UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<Void> deleteProductById(
            @Parameter(description = "Product UUID identifier", required = true)
            @PathVariable("id") String id
    ) {
        ProductId productId = ProductId.of(id);
        deleteProductByIdUseCase.execute(productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Clear all products", description = "Deletes all stored products from the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "All products cleared successfully")
    })
    public ResponseEntity<Void> clearAllProducts() {
        productRepository.clear();
        return ResponseEntity.noContent().build();
    }

    private URI resolveSearchUri(String query) {
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return URI.create(FALABELLA_SEARCH_BASE + encoded);
    }
}
