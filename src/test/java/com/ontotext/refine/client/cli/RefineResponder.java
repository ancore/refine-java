package com.ontotext.refine.client.cli;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.protocol.HttpRequestHandler;


/**
 * Responder which acts like Refine server and handles the responses for those request.
 *
 * @author Antoniy Kunchev
 */
class RefineResponder {

  private HttpServer server;

  /**
   * Starts the responder. The methods starts HTTP server on randomized port and starts it.
   *
   * @param responses for the requests
   * @throws IOException when error occurs during server creation
   */
  void start(Map<String, HttpRequestHandler> responses) throws IOException {
    server = HttpTestUtils.createTestServer(responses, 0);
    server.start();
  }

  /**
   * Shutdowns the server of the responder.
   */
  void stop() {
    if (server != null) {
      server.shutdown(200, TimeUnit.MILLISECONDS);
    }
  }

  /**
   * Provides the full URI of the responder. Example: http://localhost:8080
   *
   * @return the URI of the responder as string
   */
  String getUri() {
    return HttpTestUtils.buildUri(server, "").toString();
  }

  /**
   * Stub for the CSRF token request with the option to fail, if required. The option is controlled
   * through the context.
   *
   * @param context provides different options for the stub
   * @return request handler stub
   */
  static HttpRequestHandler csrfToken(HandlerContext context) {
    return (httpRequest, httpResponse, httpContext) -> {
      if (context.failCsrfRequest.get()) {
        httpResponse.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
      } else {
        httpResponse.setStatusCode(HttpStatus.SC_OK);
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(
            "{\"token\": \"tBElPXvVJc4K0G8YtAKSZNFvYJYtRgj1\" }".getBytes()));
        httpResponse.setEntity(entity);
      }
    };
  }

  /**
   * Used to provide various options for the request stubbing.
   *
   * @author Antoniy Kunchev
   */
  static class HandlerContext {

    private Supplier<Boolean> failCsrfRequest = () -> Boolean.FALSE;

    public HandlerContext setFailCsrfRequest(Supplier<Boolean> failCsrfRequest) {
      this.failCsrfRequest = failCsrfRequest;
      return this;
    }
  }
}
