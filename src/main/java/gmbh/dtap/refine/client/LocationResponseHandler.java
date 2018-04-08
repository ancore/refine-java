package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.RefineException;
import gmbh.dtap.refine.api.RefineProjectLocation;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;

/**
 * This class implements a {@link ResponseHandler} that expects a redirect response with location header.
 * The location header value is extracted and returned as {@link RefineProjectLocation}.
 *
 * @since 0.1.0
 */
public class LocationResponseHandler implements ResponseHandler<RefineProjectLocation> {

   /**
    * Validates the response and extracts necessary data.
    *
    * @param response the response to get the location from
    * @return the location representation
    * @throws IOException in case of an connection problem
    * @throws RefineException in case of an unexpected response or no location header is present
    * @since 0.1.0
    */
   @Override
   public RefineProjectLocation handleResponse(HttpResponse response) throws IOException {
      int status = response.getStatusLine().getStatusCode();
      if (status != 302) {
         throw new RefineException("Unexpected response status: " + status);
      }
      Header location = response.getFirstHeader("Location");
      if (location == null) {
         throw new RefineException("No location header found.");
      }
      return MinimalRefineProjectLocation.from(location.getValue());
   }
}
