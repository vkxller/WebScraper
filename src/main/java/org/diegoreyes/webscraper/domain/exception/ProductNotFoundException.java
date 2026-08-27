package org.diegoreyes.webscraper.domain.exception;

public final class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}
