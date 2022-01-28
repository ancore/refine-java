package com.ontotext.refine.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URISyntaxException;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

/**
 * Simple test for {@link RefineClients}.
 *
 * @author Antoniy Kunchev
 */
class RefineClientsTest {

  private static final String URI = "http://ontorefine.com/orefine";

  @Test
  @SuppressWarnings("deprecation")
  void create_successful() throws URISyntaxException {
    assertNotNull(assertDoesNotThrow(() -> RefineClients.create(URI)));
  }

  @Test
  void securityAware_exceptionOnMissingCredsProvider() throws URISyntaxException {
    assertThrows(NullPointerException.class, () -> RefineClients.securityAware(URI, null));
  }

  @Test
  void securityAware_successful() throws URISyntaxException {
    RefineClient client =
        assertDoesNotThrow(() -> RefineClients.securityAware(URI, new BasicCredentialsProvider()));

    assertNotNull(client);
  }

  @Test
  void custom_exceptionOnMissingClientArg() {
    assertThrows(NullPointerException.class, () -> RefineClients.custom(URI, null));
  }

  @Test
  void custom_successful() {
    RefineClient client =
        assertDoesNotThrow(() -> RefineClients.custom(URI, HttpClients.custom().build()));

    assertNotNull(client);
  }
}
