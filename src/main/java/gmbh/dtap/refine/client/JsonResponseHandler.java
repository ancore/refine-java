package gmbh.dtap.refine.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * This class implements a {@link ResponseHandler} that expects a JSON response.
 * <p>
 * The response document is parsed and checked for success/error cases.
 *
 * @since 0.1.0
 */
public class JsonResponseHandler implements ResponseHandler<String> {

   /**
    * Validates the response and extracts necessary data.
    *
    * @param response the response to get the location from
    * @return the JSON response body as string
    * @throws IOException             in case of an connection problem
    * @throws ClientProtocolException in case the server responses with an error
    * @since 0.1.0
    */
   @Override
   public String handleResponse(HttpResponse response) throws IOException {
      int status = response.getStatusLine().getStatusCode();
      if (status != 200) {
         throw new ClientProtocolException("Unexpected response status: " + status);
      }
      String responseBody = EntityUtils.toString(response.getEntity());
      JsonResponse jsonResponse;
      try {
         jsonResponse = JsonResponse.from(responseBody);
      } catch (Exception e) {
         throw new ClientProtocolException("Parser error: " + e.getMessage(), e);
      }
      if (jsonResponse.isErroneous()) {
         throw new ClientProtocolException(jsonResponse.getErrorMessage());
      } else if (!jsonResponse.isSuccessful()) {
         throw new ClientProtocolException("Unexpected response: " + jsonResponse.toString());
      }
      return responseBody;
   }
}
