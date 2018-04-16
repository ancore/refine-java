package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.RefineException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class implements a {@link ResponseHandler} that copies the response body to an {@link OutputStream}.
 *
 * @since 0.1.1
 */
class StreamResponseHandler implements ResponseHandler<Integer> {

   private final OutputStream outputStream;

   StreamResponseHandler(OutputStream outputStream) {
      this.outputStream = outputStream;
   }

   /**
    * Validates the response and writes the response body to the output stream.
    *
    * @param response the response
    * @return the number of bytes written to the output stream
    * @throws IOException in case of an connection problem
    * @throws RefineException in case of an unexpected response
    * @since 0.1.0
    */
   @Override
   public Integer handleResponse(HttpResponse response) throws IOException {
      int status = response.getStatusLine().getStatusCode();
      if (status != 200) {
         throw new RefineException("Unexpected response status: " + status);
      }
      return IOUtils.copy(response.getEntity().getContent(), outputStream);
   }
}
