package gmbh.dtap.refine.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * This class implements a {@link ResponseHandler} for the <tt>delete project</tt> response.
 *
 * @since 0.1.0
 */
public class DeleteProjectResponseHandler implements ResponseHandler<DeleteProjectResponse> {

   private ResponseParser responseParser;

   DeleteProjectResponseHandler(ResponseParser responseParser) {
      this.responseParser = responseParser;
   }

   /**
    * Validates the response and extracts necessary data.
    *
    * @param response the response to extract data from
    * @return the response representation
    * @throws IOException             in case of a connection problem
    * @throws ClientProtocolException in case the server responses with an error or is not understood
    * @since 0.1.0
    */
   @Override
   public DeleteProjectResponse handleResponse(HttpResponse response) throws IOException {
      checkStatusCode(response);
      String responseBody = EntityUtils.toString(response.getEntity());
      return responseParser.parseDeleteProjectResponse(responseBody);
   }

   private void checkStatusCode(HttpResponse response) throws IOException {
      int status = response.getStatusLine().getStatusCode();
      if (status != 200) {
         throw new ClientProtocolException("Unexpected response status: " + status);
      }
   }
}
