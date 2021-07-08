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

import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;

public class RefineClient implements AutoCloseable {

  private final URL url;
  private final HttpClient httpClient;

  RefineClient(URL url, HttpClient httpClient) {
    this.url = url;
    this.httpClient = httpClient;
  }

  public URL createUrl(String path) {
    try {
      return new URL(url, path);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler)
      throws IOException {
    return httpClient.execute(request, responseHandler);
  }

  @Override
  public void close() throws Exception {
    if (httpClient instanceof Closeable) {
      ((Closeable) httpClient).close();
    }
  }

  @Override
  public String toString() {
    return "RefineClient{" + "url=" + url + '}';
  }
}
