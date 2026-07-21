package org.diegoreyes.webscraper.port;

import java.io.IOException;
import java.net.URI;

public interface HtmlClient {

    String download(URI uri) throws IOException;
}