package gmbh.dtap.refine.client;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

/**
 * A command to retrieve Refine server version information.
 */
public class GetVersionCommand {

   private static final Logger log = LoggerFactory.getLogger(GetVersionCommand.class);

   /**
    * Executes the command.
    *
    * @param client the client to execute the command with
    * @return the result of the operation
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the server responses with an error or is not understood
    */
   public GetVersionResponse execute(RefineClient client) throws IOException {
      log.debug("execute, client={}", client);
      URL url = client.createUrl("/command/core/get-version");

      HttpUriRequest request = RequestBuilder
            .get(url.toString())
            .setHeader(ACCEPT, APPLICATION_JSON.getMimeType())
            .build();
      log.trace("request: {}", request);

      return client.execute(request, new GetVersionResponseHandler());
   }
}
