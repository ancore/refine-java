package com.ontotext.refine.client.cli;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.protocol.HttpRequestHandler;


/**
 * Utility class for common logic used in tests that require mocked HTTP server.
 *
 * @author Antoniy Kunchev
 */
public class HttpTestUtils {

  private HttpTestUtils() {
    // utility
  }

  /**
   * Creates new test server. The server is started on address 'localhost'. The input map is used to
   * set test endpoints and their handlers. The handlers are registered before the actual server
   * create. <br>
   * This method will not start the server! <br>
   * A port number of zero (0) will let the system pick up an available one and use it.
   *
   * @param handlers {@link Map} containing {@link HttpRequestHandler} for specific request paths.
   *        Every {@link Entry} represents specific path bind to specific handler
   * @param port to use
   * @return {@link HttpServer} with registered handlers for specific end-points
   */
  public static HttpServer createTestServer(Map<String, HttpRequestHandler> handlers, int port) {
    Objects.requireNonNull(handlers, "The input map is required!");
    InetSocketAddress address = new InetSocketAddress("localhost", port);
    ServerBootstrap bootstrap = ServerBootstrap.bootstrap().setLocalAddress(address.getAddress());
    handlers.forEach(bootstrap::registerHandler);
    return bootstrap.create();
  }

  /**
   * The {@link URI} that is build is based on the given {@link HttpServer} host address and port.
   * For protocol is used 'http'. The path after the base address could be configured.
   *
   * @param server from which will be retrieved the address and the port for the generated URI
   * @param path the path that should be appended after the address and the port
   * @return new URI based on the given server address and path
   * @see URI
   */
  public static URI buildUri(HttpServer server, String path) {
    Objects.requireNonNull(server, "Server is required!");
    try {
      String address = server.getInetAddress().getCanonicalHostName();
      int port = server.getLocalPort();
      return new URI("http", null, address, port, path, null, null);
    } catch (URISyntaxException uriSyntax) {
      throw new AssertionError("Error while building test URI.", uriSyntax);
    }
  }
}
