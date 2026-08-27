package org.diegoreyes.webscraper.infrastructure.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.diegoreyes.webscraper.domain.exception.InvalidProductException;
import org.diegoreyes.webscraper.domain.exception.ProductNotFoundException;
import org.diegoreyes.webscraper.infrastructure.web.dto.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidProductException(
            InvalidProductException exception,
            HttpServletRequest request
    ) {
        LOGGER.warn("Domain validation error: {}", exception.getMessage());
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleProductNotFoundException(
            ProductNotFoundException exception,
            HttpServletRequest request
    ) {
        LOGGER.warn("Product not found: {}", exception.getMessage());
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDto> handleNoSuchElementException(
            NoSuchElementException exception,
            HttpServletRequest request
    ) {
        LOGGER.warn("Resource element not found: {}", exception.getMessage());
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage() != null ? exception.getMessage() : "Resource not found",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoResourceFoundException(
            NoResourceFoundException exception,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "Resource endpoint not found: " + request.getRequestURI(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {
        LOGGER.warn("Invalid argument error: {}", exception.getMessage());
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        String validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        LOGGER.warn("Payload validation failed: {}", validationErrors);
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed: " + validationErrors,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponseDto> handleIOException(
            IOException exception,
            HttpServletRequest request
    ) {
        LOGGER.error("Scraper I/O network failure: {}", exception.getMessage(), exception);
        return buildErrorResponse(
                HttpStatus.BAD_GATEWAY,
                "Failed to download or connect to external store: " + exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(
            Exception exception,
            HttpServletRequest request
    ) {
        LOGGER.error("Unhandled internal server error: {}", exception.getMessage(), exception);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An internal error occurred. Please try again later.",
                request.getRequestURI()
        );
    }

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(
            HttpStatus status,
            String message,
            String path
    ) {
        ErrorResponseDto errorDto = ErrorResponseDto.of(
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );

        return ResponseEntity.status(status).body(errorDto);
    }
}
