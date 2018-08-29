package gmbh.dtap.refine.client;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;
import java.net.URL;

import static gmbh.dtap.refine.client.HttpParser.HTTP_PARSER;
import static org.apache.http.HttpStatus.SC_MOVED_TEMPORARILY;

/**
 * This class implements a {@link ResponseHandler} for the {@link CreateProjectResponse}.
 */
class CreateProjectResponseHandler implements ResponseHandler<CreateProjectResponse> {

   /**
    * Validates the response and extracts necessary data.
    *
    * @param response the response to get the location from
    * @return the response
    * @throws IOException     in case of an connection problem
    * @throws RefineException in case of an unexpected response or no location header is present
    */
   @Override
   public CreateProjectResponse handleResponse(HttpResponse response) throws IOException {
      HTTP_PARSER.assureStatusCode(response, SC_MOVED_TEMPORARILY);
      Header location = response.getFirstHeader("Location");
      if (location == null) {
         throw new RefineException("No location header found.");
      }
      URL url = new URL(location.getValue());
      return new CreateProjectResponse(url);
   }
}
