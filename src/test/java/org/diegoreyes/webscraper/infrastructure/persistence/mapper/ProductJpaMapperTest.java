package org.diegoreyes.webscraper.infrastructure.persistence.mapper;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;
import org.diegoreyes.webscraper.infrastructure.persistence.entity.ProductJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductJpaMapper unit tests")
class ProductJpaMapperTest {

    private final ProductJpaMapper mapper = new ProductJpaMapper();

    @Test
    @DisplayName("Should return null when mapping null product to entity")
    void shouldReturnNullWhenProductIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("Should return null when mapping null entity to domain")
    void shouldReturnNullWhenEntityIsNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("Should map domain product to JPA entity correctly")
    void shouldMapDomainToEntity() {
        ProductId id = ProductId.generate();
        Product product = new Product(
                id,
                "Falabella",
                "Notebook Gamer",
                new BigDecimal("899990.00"),
                new BigDecimal("1099990.00"),
                "-18%",
                "https://www.falabella.com/product/123",
                "https://media.falabella.com/image.jpg"
        );

        ProductJpaEntity entity = mapper.toEntity(product);

        assertNotNull(entity);
        assertEquals(id.value(), entity.getId());
        assertEquals("Falabella", entity.getStore());
        assertEquals("Notebook Gamer", entity.getName());
        assertEquals(new BigDecimal("899990.00"), entity.getPrice());
        assertEquals(new BigDecimal("1099990.00"), entity.getPreviousPrice());
        assertEquals("-18%", entity.getDiscount());
        assertEquals("https://www.falabella.com/product/123", entity.getSourceUrl());
        assertEquals("https://media.falabella.com/image.jpg", entity.getImageUrl());
    }

    @Test
    @DisplayName("Should map JPA entity to domain product correctly")
    void shouldMapEntityToDomain() {
        String idStr = ProductId.generate().value();
        ProductJpaEntity entity = new ProductJpaEntity(
                idStr,
                "Falabella",
                "Zapatilla Running",
                new BigDecimal("49990.00"),
                null,
                null,
                null,
                null
        );

        Product domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(idStr, domain.getId().value());
        assertEquals("Falabella", domain.getStore());
        assertEquals("Zapatilla Running", domain.getName());
        assertEquals(new BigDecimal("49990.00"), domain.getPrice());
        assertNull(domain.getPreviousPrice());
        assertNull(domain.getDiscount());
        assertNull(domain.getSourceUrl());
        assertNull(domain.getImageUrl());
    }
}
