package gmbh.dtap.refine.client;

import org.apache.http.HttpResponse;

import java.io.IOException;

public enum HttpParser {

   HTTP_PARSER;

   void assureStatusCode(HttpResponse response, int statusCode) throws IOException {
      if (response.getStatusLine().getStatusCode() != statusCode) {
         throw new RefineException("Unexpected response : " + response.getStatusLine());
      }
   }
}
