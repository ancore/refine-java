package gmbh.dtap.refine.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static gmbh.dtap.refine.client.HttpParser.HTTP_PARSER;
import static gmbh.dtap.refine.client.JsonParser.JSON_PARSER;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * This class implements a {@link ResponseHandler} for the {@link GetVersionResponse}.
 */
class GetVersionResponseHandler implements ResponseHandler<GetVersionResponse> {

   private static final Logger log = LoggerFactory.getLogger(GetVersionResponseHandler.class);

   /**
    * Validates the response and extracts necessary data.
    *
    * @param response the response to extract data from
    * @return the response representation
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the server responses with an unexpected status or is not understood
    */
   @Override
   public GetVersionResponse handleResponse(HttpResponse response) throws IOException {
      HTTP_PARSER.assureStatusCode(response, SC_OK);
      String responseBody = EntityUtils.toString(response.getEntity());
      log.trace("response: headers={}, body={}", response.getAllHeaders(), responseBody);
      GetVersionResponse getVersionResponse = JSON_PARSER.parseGetVersionResponse(responseBody);
      log.debug("getVersionResponse: {}", getVersionResponse);
      return getVersionResponse;
   }
}
