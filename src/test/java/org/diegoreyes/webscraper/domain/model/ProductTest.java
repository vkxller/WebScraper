package org.diegoreyes.webscraper.domain.model;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private static final String STORE =
            "Falabella";

    private static final String NAME =
            "Notebook Lenovo";

    private static final BigDecimal PRICE =
            new BigDecimal("499990");

    private static final BigDecimal PREVIOUS_PRICE =
            new BigDecimal("599990");

    private static final String DISCOUNT =
            "-17%";

    private static final String SOURCE_URL =
            "https://www.falabella.com/product/123";

    @Test
    void shouldCreateProductWithAllFields() {
        Product product = new Product(
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                DISCOUNT,
                SOURCE_URL
        );

        assertAll(
                () -> assertEquals(
                        STORE,
                        product.getStore()
                ),
                () -> assertEquals(
                        NAME,
                        product.getName()
                ),
                () -> assertEquals(
                        PRICE,
                        product.getPrice()
                ),
                () -> assertEquals(
                        PREVIOUS_PRICE,
                        product.getPreviousPrice()
                ),
                () -> assertEquals(
                        DISCOUNT,
                        product.getDiscount()
                ),
                () -> assertEquals(
                        SOURCE_URL,
                        product.getSourceUrl()
                )
        );
    }

    @Test
    void shouldCreateProductWithoutOptionalFields() {
        Product product = new Product(
                STORE,
                NAME,
                PRICE,
                null,
                null,
                null
        );

        assertAll(
                () -> assertNull(
                        product.getPreviousPrice()
                ),
                () -> assertNull(
                        product.getDiscount()
                ),
                () -> assertNull(
                        product.getSourceUrl()
                )
        );
    }

    @Test
    void shouldNormalizeBlankOptionalFieldsToNull() {
        Product product = new Product(
                STORE,
                NAME,
                PRICE,
                null,
                "   ",
                "   "
        );

        assertAll(
                () -> assertNull(
                        product.getDiscount()
                ),
                () -> assertNull(
                        product.getSourceUrl()
                )
        );
    }

    @Test
    void shouldTrimTextFields() {
        Product product = new Product(
                "  Falabella  ",
                "  Notebook Lenovo  ",
                PRICE,
                PREVIOUS_PRICE,
                "  -17%  ",
                "  https://www.falabella.com/product/123  "
        );

        assertAll(
                () -> assertEquals(
                        STORE,
                        product.getStore()
                ),
                () -> assertEquals(
                        NAME,
                        product.getName()
                ),
                () -> assertEquals(
                        DISCOUNT,
                        product.getDiscount()
                ),
                () -> assertEquals(
                        SOURCE_URL,
                        product.getSourceUrl()
                )
        );
    }

    @Test
    void shouldAcceptZeroPrice() {
        Product product = new Product(
                STORE,
                NAME,
                BigDecimal.ZERO,
                PREVIOUS_PRICE,
                DISCOUNT,
                SOURCE_URL
        );

        assertEquals(
                BigDecimal.ZERO,
                product.getPrice()
        );
    }

    @Test
    void shouldAcceptZeroPreviousPrice() {
        Product product = new Product(
                STORE,
                NAME,
                PRICE,
                BigDecimal.ZERO,
                DISCOUNT,
                SOURCE_URL
        );

        assertEquals(
                BigDecimal.ZERO,
                product.getPreviousPrice()
        );
    }

    @Test
    void shouldRejectNullStore() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        null,
                        NAME,
                        PRICE,
                        PREVIOUS_PRICE,
                        DISCOUNT,
                        SOURCE_URL
                )
        );

        assertEquals(
                "Store must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectBlankStore() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        "   ",
                        NAME,
                        PRICE,
                        PREVIOUS_PRICE,
                        DISCOUNT,
                        SOURCE_URL
                )
        );

        assertEquals(
                "Store must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullName() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        STORE,
                        null,
                        PRICE,
                        PREVIOUS_PRICE,
                        DISCOUNT,
                        SOURCE_URL
                )
        );

        assertEquals(
                "Product name must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectBlankName() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        STORE,
                        "   ",
                        PRICE,
                        PREVIOUS_PRICE,
                        DISCOUNT,
                        SOURCE_URL
                )
        );

        assertEquals(
                "Product name must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullPrice() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        STORE,
                        NAME,
                        null,
                        PREVIOUS_PRICE,
                        DISCOUNT,
                        SOURCE_URL
                )
        );

        assertEquals(
                "Price must be greater than or equal to zero",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNegativePrice() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        STORE,
                        NAME,
                        new BigDecimal("-1"),
                        PREVIOUS_PRICE,
                        DISCOUNT,
                        SOURCE_URL
                )
        );

        assertEquals(
                "Price must be greater than or equal to zero",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNegativePreviousPrice() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        STORE,
                        NAME,
                        PRICE,
                        new BigDecimal("-1"),
                        DISCOUNT,
                        SOURCE_URL
                )
        );

        assertEquals(
                "Previous price must be greater than or equal to zero",
                exception.getMessage()
        );
    }

    @Test
    void shouldBeEqualWhenAllFieldsAreEqual() {
        Product firstProduct = createProduct();
        Product secondProduct = createProduct();

        assertAll(
                () -> assertEquals(
                        firstProduct,
                        secondProduct
                ),
                () -> assertEquals(
                        firstProduct.hashCode(),
                        secondProduct.hashCode()
                )
        );
    }

    @Test
    void shouldBeEqualToItself() {
        Product product = createProduct();

        assertEquals(
                product,
                product
        );
    }

    @Test
    void shouldNotBeEqualToNull() {
        Product product = createProduct();

        assertNotEquals(
                product,
                null
        );
    }

    @Test
    void shouldNotBeEqualToAnotherType() {
        Product product = createProduct();

        assertNotEquals(
                product,
                "Product"
        );
    }

    @Test
    void shouldNotBeEqualWhenStoreIsDifferent() {
        Product differentProduct = new Product(
                "Ripley",
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                DISCOUNT,
                SOURCE_URL
        );

        assertNotEquals(
                createProduct(),
                differentProduct
        );
    }

    @Test
    void shouldNotBeEqualWhenNameIsDifferent() {
        Product differentProduct = new Product(
                STORE,
                "Notebook HP",
                PRICE,
                PREVIOUS_PRICE,
                DISCOUNT,
                SOURCE_URL
        );

        assertNotEquals(
                createProduct(),
                differentProduct
        );
    }

    @Test
    void shouldNotBeEqualWhenPriceIsDifferent() {
        Product differentProduct = new Product(
                STORE,
                NAME,
                new BigDecimal("399990"),
                PREVIOUS_PRICE,
                DISCOUNT,
                SOURCE_URL
        );

        assertNotEquals(
                createProduct(),
                differentProduct
        );
    }

    @Test
    void shouldNotBeEqualWhenPreviousPriceIsDifferent() {
        Product differentProduct = new Product(
                STORE,
                NAME,
                PRICE,
                new BigDecimal("699990"),
                DISCOUNT,
                SOURCE_URL
        );

        assertNotEquals(
                createProduct(),
                differentProduct
        );
    }

    @Test
    void shouldNotBeEqualWhenDiscountIsDifferent() {
        Product differentProduct = new Product(
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                "-10%",
                SOURCE_URL
        );

        assertNotEquals(
                createProduct(),
                differentProduct
        );
    }

    @Test
    void shouldNotBeEqualWhenSourceUrlIsDifferent() {
        Product differentProduct = new Product(
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                DISCOUNT,
                "https://www.falabella.com/product/999"
        );

        assertNotEquals(
                createProduct(),
                differentProduct
        );
    }

    @Test
    void shouldBeEqualWhenAllOptionalFieldsAreNull() {
        Product firstProduct = new Product(
                STORE,
                NAME,
                PRICE,
                null,
                null,
                null
        );

        Product secondProduct = new Product(
                STORE,
                NAME,
                PRICE,
                null,
                null,
                null
        );

        assertAll(
                () -> assertEquals(
                        firstProduct,
                        secondProduct
                ),
                () -> assertEquals(
                        firstProduct.hashCode(),
                        secondProduct.hashCode()
                )
        );
    }

    @Test
    void shouldNotBeEqualWhenFirstPreviousPriceIsNull() {
        Product firstProduct = new Product(
                STORE,
                NAME,
                PRICE,
                null,
                DISCOUNT,
                SOURCE_URL
        );

        Product secondProduct = createProduct();

        assertNotEquals(
                firstProduct,
                secondProduct
        );
    }

    @Test
    void shouldNotBeEqualWhenSecondPreviousPriceIsNull() {
        Product firstProduct = createProduct();

        Product secondProduct = new Product(
                STORE,
                NAME,
                PRICE,
                null,
                DISCOUNT,
                SOURCE_URL
        );

        assertNotEquals(
                firstProduct,
                secondProduct
        );
    }

    @Test
    void shouldNotBeEqualWhenFirstDiscountIsNull() {
        Product firstProduct = new Product(
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                null,
                SOURCE_URL
        );

        Product secondProduct = createProduct();

        assertNotEquals(
                firstProduct,
                secondProduct
        );
    }

    @Test
    void shouldNotBeEqualWhenSecondDiscountIsNull() {
        Product firstProduct = createProduct();

        Product secondProduct = new Product(
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                null,
                SOURCE_URL
        );

        assertNotEquals(
                firstProduct,
                secondProduct
        );
    }

    @Test
    void shouldNotBeEqualWhenFirstSourceUrlIsNull() {
        Product firstProduct = createProduct(null);
        Product secondProduct = createProduct();

        assertNotEquals(
                firstProduct,
                secondProduct
        );
    }

    @Test
    void shouldNotBeEqualWhenSecondSourceUrlIsNull() {
        Product firstProduct = createProduct();
        Product secondProduct = createProduct(null);

        assertNotEquals(
                firstProduct,
                secondProduct
        );
    }

    @Test
    void shouldContainProductDataInToString() {
        String result = createProduct().toString();

        assertAll(
                () -> assertTrue(
                        result.contains(STORE)
                ),
                () -> assertTrue(
                        result.contains(NAME)
                ),
                () -> assertTrue(
                        result.contains(PRICE.toString())
                ),
                () -> assertTrue(
                        result.contains(PREVIOUS_PRICE.toString())
                ),
                () -> assertTrue(
                        result.contains(DISCOUNT)
                ),
                () -> assertTrue(
                        result.contains(SOURCE_URL)
                )
        );
    }

    @Test
    void shouldRepresentNullOptionalFieldsInToString() {
        Product product = new Product(
                STORE,
                NAME,
                PRICE,
                null,
                null,
                null
        );

        String result = product.toString();

        assertAll(
                () -> assertTrue(
                        result.contains("previousPrice=null")
                ),
                () -> assertTrue(
                        result.contains("discount='null'")
                ),
                () -> assertTrue(
                        result.contains("sourceUrl='null'")
                )
        );
    }

    private Product createProduct() {
        return createProduct(SOURCE_URL);
    }

    private Product createProduct(String sourceUrl) {
        return new Product(
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                DISCOUNT,
                sourceUrl
        );
    }
}