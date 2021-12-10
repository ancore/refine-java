package com.ontotext.refine.client;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.impl.client.HttpClients;

public interface RefineClients {

  static RefineClient create(String url) throws MalformedURLException {
    // TODO configure some sensible timeouts and other configurations
    return new RefineClient(new URL(url), HttpClients.createDefault());
  }
}
