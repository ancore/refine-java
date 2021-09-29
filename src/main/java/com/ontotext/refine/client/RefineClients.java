package com.ontotext.refine.client;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.impl.client.HttpClients;

public interface RefineClients {

  static RefineClient create(String uri) throws URISyntaxException {
    // TODO configure some sensible timeouts and other configurations
    return new RefineClient(new URI(uri), HttpClients.createDefault());
  }
}
