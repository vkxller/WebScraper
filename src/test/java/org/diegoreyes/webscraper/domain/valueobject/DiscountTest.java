package org.diegoreyes.webscraper.domain.valueobject;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountTest {

    @Test
    void shouldCreateValidDiscount() {
        Discount discount = Discount.of("-17%");
        assertNotNull(discount);
        assertEquals("-17%", discount.value());
        assertEquals("-17%", discount.toString());
    }

    @Test
    void shouldReturnNullForNullOrBlankInFactory() {
        assertNull(Discount.of(null));
        assertNull(Discount.of(""));
        assertNull(Discount.of("   "));
    }

    @Test
    void shouldTrimDiscount() {
        Discount discount = new Discount("  -20%  ");
        assertEquals("-20%", discount.value());
    }

    @Test
    void shouldRejectNullDiscountInConstructor() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Discount(null)
        );
        assertEquals("Discount must not be blank", exception.getMessage());
    }

    @Test
    void shouldRejectBlankDiscountInConstructor() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Discount("   ")
        );
        assertEquals("Discount must not be blank", exception.getMessage());
    }

    @Test
    void shouldBeEqualForSameValue() {
        Discount d1 = Discount.of("-15%");
        Discount d2 = Discount.of("-15%");
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentValue() {
        Discount d1 = Discount.of("-15%");
        Discount d2 = Discount.of("-25%");
        assertNotEquals(d1, d2);
    }
}
