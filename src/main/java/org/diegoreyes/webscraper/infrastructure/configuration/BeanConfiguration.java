package org.diegoreyes.webscraper.infrastructure.configuration;

import org.diegoreyes.webscraper.application.ProductScraperService;
import org.diegoreyes.webscraper.application.usecase.DeleteProductByIdUseCase;
import org.diegoreyes.webscraper.application.usecase.GetAllProductsUseCase;
import org.diegoreyes.webscraper.application.usecase.GetProductByIdUseCase;
import org.diegoreyes.webscraper.application.usecase.ScrapeAndSaveProductsUseCase;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.infrastructure.client.JsoupHtmlClient;
import org.diegoreyes.webscraper.infrastructure.parser.FalabellaProductParser;
import org.diegoreyes.webscraper.port.HtmlClient;
import org.diegoreyes.webscraper.port.ProductParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public HtmlClient htmlClient() {
        return new JsoupHtmlClient();
    }

    @Bean
    public ProductParser productParser() {
        return new FalabellaProductParser();
    }

    @Bean
    public GetAllProductsUseCase getAllProductsUseCase(ProductRepository productRepository) {
        return new GetAllProductsUseCase(productRepository);
    }

    @Bean
    public GetProductByIdUseCase getProductByIdUseCase(ProductRepository productRepository) {
        return new GetProductByIdUseCase(productRepository);
    }

    @Bean
    public DeleteProductByIdUseCase deleteProductByIdUseCase(ProductRepository productRepository) {
        return new DeleteProductByIdUseCase(productRepository);
    }

    @Bean
    public ScrapeAndSaveProductsUseCase scrapeAndSaveProductsUseCase(
            HtmlClient htmlClient,
            ProductParser productParser,
            ProductRepository productRepository
    ) {
        return new ScrapeAndSaveProductsUseCase(htmlClient, productParser, productRepository);
    }

    @Bean
    public ProductScraperService productScraperService(
            HtmlClient htmlClient,
            ProductParser productParser,
            ProductRepository productRepository
    ) {
        return new ProductScraperService(htmlClient, productParser, productRepository);
    }
}
