package org.diegoreyes.webscraper.domain.valueobject;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductIdTest {

    @Test
    void shouldCreateValidProductId() {
        ProductId id = ProductId.of("prod-123");
        assertEquals("prod-123", id.value());
        assertEquals("prod-123", id.toString());
    }

    @Test
    void shouldGenerateRandomProductId() {
        ProductId id = ProductId.generate();
        assertNotNull(id);
        assertNotNull(id.value());
        assertFalse(id.value().isBlank());
    }

    @Test
    void shouldTrimProductId() {
        ProductId id = new ProductId("  abc-456  ");
        assertEquals("abc-456", id.value());
    }

    @Test
    void shouldRejectNullProductId() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new ProductId(null)
        );
        assertEquals("Product ID must not be blank", exception.getMessage());
    }

    @Test
    void shouldRejectBlankProductId() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new ProductId("   ")
        );
        assertEquals("Product ID must not be blank", exception.getMessage());
    }

    @Test
    void shouldBeEqualForSameValue() {
        ProductId id1 = ProductId.of("id-1");
        ProductId id2 = ProductId.of("id-1");
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentValue() {
        ProductId id1 = ProductId.of("id-1");
        ProductId id2 = ProductId.of("id-2");
        assertNotEquals(id1, id2);
    }
}
