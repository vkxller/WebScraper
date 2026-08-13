package org.diegoreyes.webscraper.domain.valueobject;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PriceTest {

    @Test
    void shouldCreateValidPriceFromBigDecimal() {
        BigDecimal amount = new BigDecimal("499990");
        Price price = Price.of(amount);
        assertNotNull(price);
        assertEquals(amount, price.amount());
        assertEquals("499990", price.toString());
    }

    @Test
    void shouldCreateValidPriceFromString() {
        Price price = Price.of("499990");
        assertEquals(new BigDecimal("499990"), price.amount());
    }

    @Test
    void shouldCreateZeroPrice() {
        Price price = Price.zero();
        assertEquals(BigDecimal.ZERO, price.amount());
    }

    @Test
    void shouldReturnNullForNullBigDecimalInFactory() {
        assertNull(Price.of((BigDecimal) null));
    }

    @Test
    void shouldRejectNullPrice() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Price(null)
        );
        assertEquals("Price must be greater than or equal to zero", exception.getMessage());
    }

    @Test
    void shouldRejectNegativePrice() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Price(new BigDecimal("-1"))
        );
        assertEquals("Price must be greater than or equal to zero", exception.getMessage());
    }

    @Test
    void shouldRejectNullStringPrice() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> Price.of((String) null)
        );
        assertEquals("Price must be greater than or equal to zero", exception.getMessage());
    }

    @Test
    void shouldRejectBlankStringPrice() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> Price.of("   ")
        );
        assertEquals("Price must be greater than or equal to zero", exception.getMessage());
    }

    @Test
    void shouldRejectInvalidNumberFormatStringPrice() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> Price.of("not-a-number")
        );
        assertEquals("Price format is invalid", exception.getMessage());
    }

    @Test
    void shouldBeEqualForSameAmount() {
        Price price1 = Price.of(new BigDecimal("1000"));
        Price price2 = Price.of(new BigDecimal("1000"));
        assertEquals(price1, price2);
        assertEquals(price1.hashCode(), price2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentAmount() {
        Price price1 = Price.of(new BigDecimal("1000"));
        Price price2 = Price.of(new BigDecimal("2000"));
        assertNotEquals(price1, price2);
    }
}
