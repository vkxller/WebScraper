package org.diegoreyes.webscraper.port;

import org.diegoreyes.webscraper.domain.model.Product;

import java.util.List;

public interface ProductParser {

    List<Product> parse(String html);
}