package com.ontotext.refine.client;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

/**
 * Defines different instances of {@link RefineClient}.
 *
 * @author Antoniy Kunchev
 */
// TODO configure some sensible timeouts and other configurations
public interface RefineClients {

  /**
   * Creates default {@link RefineClient} instance.
   *
   * @deprecated use {@link #standard(String)} instead
   * @param uri to be used as base for the commands execution. Basically the address of the Refine
   *        tool instance
   * @return new default {@link RefineClient} instance
   * @throws URISyntaxException when the input <code>uri</code> argument is invalid
   */
  @Deprecated(since = "1.4", forRemoval = true)
  static RefineClient create(String uri) throws URISyntaxException {
    return standard(uri);
  }

  /**
   * Creates default {@link RefineClient} instance.
   *
   * @param uri to be used as base for the commands execution. Basically the address of the Refine
   *        tool instance
   * @return new default {@link RefineClient} instance
   * @throws URISyntaxException when the input <code>uri</code> argument is invalid
   */
  static RefineClient standard(String uri) throws URISyntaxException {
    return new RefineClient(new URI(uri), HttpClients.createDefault());
  }

  /**
   * Creates secured {@link RefineClient} instance. It uses the given {@link CredentialsProvider}
   * for authentication, when the commands are executed.
   *
   * @param uri to be used as base for the commands execution. Basically the address of the Refine
   *        tool instance
   * @param credsProvider for the credentials that must be used when the commands are executed
   * @return new {@link RefineClient} instance
   * @throws URISyntaxException when the input <code>uri</code> argument is invalid
   * @throws NullPointerException if the <code>credsProvider</code> argument is <code>null</code>
   */
  static RefineClient securityAware(String uri, CredentialsProvider credsProvider)
      throws URISyntaxException {
    Validate.notNull(credsProvider, "The credentials provider is required.");
    HttpClientBuilder client = HttpClients.custom().setDefaultCredentialsProvider(credsProvider);
    return new RefineClient(new URI(uri), client.build());
  }

  /**
   * Creates {@link RefineClient} instance using the provided {@link CloseableHttpClient}. This
   * allows more freedom, when configuring the behavior of the client.
   *
   * @param uri to be used as base for the commands execution. Basically the address of the Refine
   *        tool instance
   * @param internalClient to be used for execution of the requests to the Refine tool
   * @return new {@link RefineClient} instance
   * @throws URISyntaxException when the input <code>uri</code> argument is invalid
   */
  static RefineClient custom(String uri, CloseableHttpClient internalClient)
      throws URISyntaxException {
    Validate.notNull(internalClient, "The internal client argument is required.");
    return new RefineClient(new URI(uri), internalClient);
  }
}
