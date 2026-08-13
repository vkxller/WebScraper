package org.diegoreyes.webscraper.domain.valueobject;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductNameTest {

    @Test
    void shouldCreateValidProductName() {
        ProductName name = ProductName.of("Notebook Lenovo");
        assertEquals("Notebook Lenovo", name.value());
        assertEquals("Notebook Lenovo", name.toString());
    }

    @Test
    void shouldTrimProductName() {
        ProductName name = new ProductName("  Notebook Lenovo  ");
        assertEquals("Notebook Lenovo", name.value());
    }

    @Test
    void shouldRejectNullProductName() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new ProductName(null)
        );
        assertEquals("Product name must not be blank", exception.getMessage());
    }

    @Test
    void shouldRejectBlankProductName() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new ProductName("   ")
        );
        assertEquals("Product name must not be blank", exception.getMessage());
    }

    @Test
    void shouldBeEqualForSameValue() {
        ProductName name1 = ProductName.of("Notebook Lenovo");
        ProductName name2 = ProductName.of("Notebook Lenovo");
        assertEquals(name1, name2);
        assertEquals(name1.hashCode(), name2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentValue() {
        ProductName name1 = ProductName.of("Notebook Lenovo");
        ProductName name2 = ProductName.of("Notebook HP");
        assertNotEquals(name1, name2);
    }
}
