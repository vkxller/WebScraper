package org.diegoreyes.webscraper.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidProductExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        InvalidProductException exception =
                new InvalidProductException("Validation error");

        assertEquals("Validation error", exception.getMessage());
    }
}
