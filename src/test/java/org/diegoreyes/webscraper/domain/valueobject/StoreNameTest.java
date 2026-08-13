package org.diegoreyes.webscraper.domain.valueobject;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoreNameTest {

    @Test
    void shouldCreateValidStoreName() {
        StoreName store = StoreName.of("Falabella");
        assertEquals("Falabella", store.value());
        assertEquals("Falabella", store.toString());
    }

    @Test
    void shouldTrimStoreName() {
        StoreName store = new StoreName("  Falabella  ");
        assertEquals("Falabella", store.value());
    }

    @Test
    void shouldRejectNullStoreName() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new StoreName(null)
        );
        assertEquals("Store must not be blank", exception.getMessage());
    }

    @Test
    void shouldRejectBlankStoreName() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new StoreName("   ")
        );
        assertEquals("Store must not be blank", exception.getMessage());
    }

    @Test
    void shouldBeEqualForSameValue() {
        StoreName store1 = StoreName.of("Falabella");
        StoreName store2 = StoreName.of("Falabella");
        assertEquals(store1, store2);
        assertEquals(store1.hashCode(), store2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentValue() {
        StoreName store1 = StoreName.of("Falabella");
        StoreName store2 = StoreName.of("Ripley");
        assertNotEquals(store1, store2);
    }
}
