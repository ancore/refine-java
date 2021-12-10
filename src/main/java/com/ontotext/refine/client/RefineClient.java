package com.ontotext.refine.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;


/**
 * Represents the client which purpose is to provide the HTTP communication between the application
 * and the Refine instance.
 */
public class RefineClient implements Closeable {

  private final URL url;
  private final CloseableHttpClient httpClient;

  /**
   * Creates new client instance.
   *
   * @param url where the Refine instance could be accessed
   * @param httpClient used to executed the requests
   */
  RefineClient(URL url, CloseableHttpClient httpClient) {
    this.url = url;
    this.httpClient = httpClient;
  }

  /**
   * Creates URL from the provided path and the base URL of the current client.
   *
   * @param path to be added to the base URL
   * @return new {@link URL}
   */
  public URL createUrl(String path) {
    try {
      return new URL(url, path);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
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
    return "RefineClient{" + "url=" + url + '}';
  }
}
