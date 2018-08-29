package gmbh.dtap.refine.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static gmbh.dtap.refine.client.HttpParser.HTTP_PARSER;
import static gmbh.dtap.refine.client.JsonParser.JSON_PARSER;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * This class implements a {@link ResponseHandler} for the {@link ExpressionPreviewResponse}.
 */
class ExpressionPreviewResponseHandler implements ResponseHandler<ExpressionPreviewResponse> {

   /**
    * Validates the response and extracts necessary data.
    *
    * @param response the response to extract data from
    * @return the response representation
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the server responses with an unexpected status or is not understood
    */
   @Override
   public ExpressionPreviewResponse handleResponse(HttpResponse response) throws IOException {
      HTTP_PARSER.assureStatusCode(response, SC_OK);
      String responseBody = EntityUtils.toString(response.getEntity());
      return JSON_PARSER.parseExpressionPreviewResponse(responseBody);
   }
}
