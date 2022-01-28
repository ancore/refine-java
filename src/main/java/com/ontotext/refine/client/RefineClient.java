package com.ontotext.refine.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Represents the client which purpose is to provide the HTTP communication between the application
 * and the Refine instance.
 */
public class RefineClient implements Closeable {

  private final URI uri;
  private final CloseableHttpClient httpClient;

  /**
   * Creates new client instance.
   *
   * @param uri where the Refine instance could be accessed
   * @param httpClient used to executed the requests
   */
  RefineClient(URI uri, CloseableHttpClient httpClient) {
    this.uri = uri;
    this.httpClient = httpClient;
  }

  /**
   * Creates URI from the provided path and the base URI of the current client.
   *
   * @param path to be added to the base URI
   * @return new {@link URI}
   */
  public URI createUri(String path) {
    try {
      return new URIBuilder(uri).setPath(path).build();
    } catch (URISyntaxException uriExc) {
      throw new IllegalArgumentException(uriExc);
    }
  }

  /**
   * Executes given {@link HttpUriRequest}.
   *
   * @param <T> the type of the response
   * @param request which should be executed
   * @param responseHandler which is used to process the response from the request
   * @return a response
   * @throws IOException when there is an error during execution
   */
  public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler)
      throws IOException {
    return httpClient.execute(request, responseHandler);
  }

  @Override
  public void close() throws IOException {
    httpClient.close();
  }

  @Override
  public String toString() {
    return "RefineClient{" + "url=" + uri + '}';
  }
}
