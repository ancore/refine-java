package com.ontotext.refine.client.cli;

import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HttpRequestHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import picocli.CommandLine.ExitCode;


/**
 * Test for {@link ExportOperations}.
 *
 * @author Antoniy Kunchev
 */
class ExportOperationsTest extends BaseProcessTest {

  private static RefineResponder responder = new RefineResponder();
  private static boolean shouldFailExportRequest;

  @BeforeAll
  static void beforeAll() throws IOException {
    shouldFailExportRequest = false;
    responder.start(mockResponses());
  }

  @AfterAll
  static void cleanup() {
    responder.stop();
  }

  @Override
  protected Consumer<String[]> commandExecutor() {
    return ExportOperations::main;
  }

  @Test
  @ExpectSystemExit(ExitCode.USAGE)
  void shouldExitWithErrorOnMissingProjectArg() {
    try {
      commandExecutor().accept(args("-u " + responder.getUri()));
    } finally {
      assertTrue(assertMissingArgError().contains("PROJECT"));
    }
  }

  @Test
  @ExpectSystemExit(ExitCode.SOFTWARE)
  void shouldFailOnRefineRequestFailure() {
    try {
      shouldFailExportRequest = true;

      commandExecutor().accept(args("1812661014997", "-u " + responder.getUri()));
    } finally {
      shouldFailExportRequest = false;

      assertEquals(
          "Failed to retrieve the operations for project: '1812661014997' due to:"
              + " Unexpected response : HTTP/1.1 500 Internal Server Error",
          consoleErrors().stripTrailing());
    }
  }

  @Test
  @ExpectSystemExit(ExitCode.OK)
  void shouldPassSuccessfully() {
    try {
      commandExecutor().accept(args("1812661014997", "-u " + responder.getUri()));
    } finally {
      String errors = consoleErrors();
      assertTrue(errors.isEmpty(), "Expected no errors but there were: " + errors);

      assertFalse(consoleOutput().isEmpty(), "The output should not be empty, but it was.");
    }
  }

  private static Map<String, HttpRequestHandler> mockResponses() {
    return singletonMap("/orefine/command/core/get-operations", exportOperationsHandler());
  }

  private static HttpRequestHandler exportOperationsHandler() {
    return (request, response, context) -> {
      if (shouldFailExportRequest) {
        response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
      } else {
        response.setStatusCode(HttpStatus.SC_OK);
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream stream =
            ExportRdfTest.class.getClassLoader().getResourceAsStream("operations.json");
        entity.setContent(stream);
        entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setEntity(entity);
      }
    };
  }

}
