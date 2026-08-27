package org.diegoreyes.webscraper.infrastructure.web.exception;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;
import org.diegoreyes.webscraper.domain.exception.ProductNotFoundException;
import org.diegoreyes.webscraper.infrastructure.web.dto.ErrorResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GlobalExceptionHandler unit tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
        request.setRequestURI("/api/products/test");
    }

    @Test
    @DisplayName("Should handle InvalidProductException as 400 Bad Request")
    void shouldHandleInvalidProductException() {
        InvalidProductException exception = new InvalidProductException("Invalid product name");

        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleInvalidProductException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().status());
        assertEquals("Invalid product name", response.getBody().message());
        assertEquals("/api/products/test", response.getBody().path());
    }

    @Test
    @DisplayName("Should handle ProductNotFoundException as 404 Not Found")
    void shouldHandleProductNotFoundException() {
        ProductNotFoundException exception = new ProductNotFoundException("Product not found");

        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleProductNotFoundException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertEquals("Product not found", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle NoSuchElementException as 404 Not Found")
    void shouldHandleNoSuchElementException() {
        NoSuchElementException exception = new NoSuchElementException("Element absent");

        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleNoSuchElementException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Element absent", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle NoResourceFoundException as 404 Not Found")
    void shouldHandleNoResourceFoundException() {
        NoResourceFoundException exception = new NoResourceFoundException(HttpMethod.GET, "/api/unknown");
        request.setRequestURI("/api/unknown");

        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleNoResourceFoundException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().message().contains("Resource endpoint not found"));
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException as 400 Bad Request")
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Bad input argument");

        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleIllegalArgumentException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad input argument", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException as 400 Bad Request")
    void shouldHandleMethodArgumentNotValidException() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "scrapeRequestDto");
        bindingResult.addError(new FieldError("scrapeRequestDto", "query", "cannot exceed 200 characters"));

        Method method = getClass().getDeclaredMethod("sampleHandlerMethod", String.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleValidationException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().message().contains("cannot exceed 200 characters"));
    }

    @Test
    @DisplayName("Should handle IOException as 502 Bad Gateway")
    void shouldHandleIOException() {
        IOException exception = new IOException("Connection timeout to store");

        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleIOException(exception, request);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertTrue(response.getBody().message().contains("Failed to download"));
    }

    @Test
    @DisplayName("Should handle generic Exception as 500 Internal Server Error")
    void shouldHandleGenericException() {
        RuntimeException exception = new RuntimeException("Unexpected fatal failure");

        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleGenericException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An internal error occurred. Please try again later.", response.getBody().message());
    }

    @SuppressWarnings("unused")
    private void sampleHandlerMethod(String parameter) {
    }
}
