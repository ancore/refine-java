/*
 * Copyright 2019 DTAP GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ontotext.refine.client;

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
public class RefineClient implements AutoCloseable {

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
  public void close() throws Exception {
    httpClient.close();
  }

  @Override
  public String toString() {
    return "RefineClient{" + "url=" + url + '}';
  }
}
