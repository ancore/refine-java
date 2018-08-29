package gmbh.dtap.refine.client;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class RefineClient implements AutoCloseable {

   private final URL url;
   private final HttpClient httpClient;

   RefineClient(URL url, HttpClient httpClient) {
      this.url = url;
      this.httpClient = httpClient;
   }

   URL createUrl(String path) {
      try {
         return new URL(url, path);
      } catch (MalformedURLException e) {
         throw new IllegalArgumentException(e);
      }
   }

   <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
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
