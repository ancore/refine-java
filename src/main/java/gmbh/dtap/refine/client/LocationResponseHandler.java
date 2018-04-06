package gmbh.dtap.refine.client;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;

/**
 * This class implements a {@link ResponseHandler} that expects a redirect response with location header.
 * The location header value is extracted and returned.
 *
 * @since 0.1.0
 */
public class LocationResponseHandler implements ResponseHandler<String> {

   /**
    * Validates the response and extracts necessary data.
    *
    * @param response the response to get the location from
    * @return the value of the location header
    * @throws IOException             in case of an connection problem
    * @throws ClientProtocolException in case of an unexpected response or no location header is present
    * @since 0.1.0
    */
   @Override
   public String handleResponse(HttpResponse response) throws IOException {
      int status = response.getStatusLine().getStatusCode();
      if (status != 302) {
         throw new ClientProtocolException("Unexpected response status: " + status);
      }
      Header location = response.getFirstHeader("Location");
      if (location == null) {
         throw new ClientProtocolException("No location header found.");
      }
      return location.getValue();
   }
}
