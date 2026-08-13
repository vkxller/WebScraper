package org.diegoreyes.webscraper.domain.valueobject;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductUrlTest {

    @Test
    void shouldCreateValidProductUrl() {
        ProductUrl url = ProductUrl.of("https://www.falabella.com/product/123");
        assertNotNull(url);
        assertEquals("https://www.falabella.com/product/123", url.value());
        assertEquals("https://www.falabella.com/product/123", url.toString());
    }

    @Test
    void shouldReturnNullForNullOrBlankInFactory() {
        assertNull(ProductUrl.of(null));
        assertNull(ProductUrl.of(""));
        assertNull(ProductUrl.of("   "));
    }

    @Test
    void shouldTrimProductUrl() {
        ProductUrl url = new ProductUrl("  https://www.falabella.com/product/123  ");
        assertEquals("https://www.falabella.com/product/123", url.value());
    }

    @Test
    void shouldRejectNullProductUrlInConstructor() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new ProductUrl(null)
        );
        assertEquals("Product URL must not be blank", exception.getMessage());
    }

    @Test
    void shouldRejectBlankProductUrlInConstructor() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new ProductUrl("   ")
        );
        assertEquals("Product URL must not be blank", exception.getMessage());
    }

    @Test
    void shouldBeEqualForSameValue() {
        ProductUrl u1 = ProductUrl.of("https://falabella.com/1");
        ProductUrl u2 = ProductUrl.of("https://falabella.com/1");
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentValue() {
        ProductUrl u1 = ProductUrl.of("https://falabella.com/1");
        ProductUrl u2 = ProductUrl.of("https://falabella.com/2");
        assertNotEquals(u1, u2);
    }
}
