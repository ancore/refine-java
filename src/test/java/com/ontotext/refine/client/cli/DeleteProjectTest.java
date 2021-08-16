package com.ontotext.refine.client.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ontotext.refine.client.cli.RefineResponder.HandlerContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.protocol.HttpRequestHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import picocli.CommandLine.ExitCode;


/**
 * Test for {@link DeleteProject}.
 *
 * @author Antoniy Kunchev
 */
class DeleteProjectTest extends BaseProcessTest {

  private static RefineResponder responder = new RefineResponder();
  private static boolean failCsrfRequest;
  private static boolean shouldRespondWithError;

  @BeforeAll
  static void beforeAll() throws IOException {
    failCsrfRequest = false;
    shouldRespondWithError = false;
    responder.start(mockResponses());
  }

  @AfterAll
  static void cleanup() {
    responder.stop();
  }

  @Override
  protected Consumer<String[]> commandExecutor() {
    return DeleteProject::main;
  }

  @Test
  @ExpectSystemExit(ExitCode.USAGE)
  void shouldExitWithErrorOnMissingFileArg() {
    try {
      commandExecutor().accept(args("-u " + responder.getUri()));
    } finally {
      assertTrue(assertMissingArgError().contains("PROJECT"));
    }
  }

  @Test
  @ExpectSystemExit(ExitCode.SOFTWARE)
  void shouldFailToGetCrsfToken() {
    try {
      failCsrfRequest = true;

      commandExecutor().accept(args("1812661014997", "-u " + responder.getUri()));
    } finally {
      failCsrfRequest = false;

      String error = consoleErrors().split(System.lineSeparator())[0];
      assertEquals(
          "Failed to retrieve CSRF token due to: Unexpected response :"
              + " HTTP/1.1 500 Internal Server Error",
          error);
    }
  }

  @Test
  @ExpectSystemExit(ExitCode.SOFTWARE)
  void shouldReturnError() {
    try {
      shouldRespondWithError = true;

      commandExecutor().accept(args("1812661014997", "-u " + responder.getUri()));
    } finally {
      shouldRespondWithError = false;

      String error = consoleErrors().split(System.lineSeparator())[0];
      assertEquals("Failed to delete project with id '1812661014997' due to: Failed!", error);
    }
  }

  @Test
  @ExpectSystemExit(ExitCode.OK)
  void shouldPassSuccessfully() {
    try {
      commandExecutor().accept(args("1812661014997", "-u " + responder.getUri()));
    } finally {
      assertEquals(
          "Successfully deleted project with id: '1812661014997'", consoleOutput().stripTrailing());
    }
  }

  private static Map<String, HttpRequestHandler> mockResponses() {
    Map<String, HttpRequestHandler> responses = new HashMap<>(2);
    HandlerContext context = new HandlerContext().setFailCsrfRequest(() -> failCsrfRequest);
    responses.put("/orefine/command/core/get-csrf-token", RefineResponder.csrfToken(context));
    responses.put("/orefine/command/core/delete-project", deleteProjectHandler());
    return responses;
  }

  private static HttpRequestHandler deleteProjectHandler() {
    return (httpRequest, httpResponse, httpContext) -> {
      httpResponse.setStatusCode(HttpStatus.SC_OK);
      BasicHttpEntity entity = new BasicHttpEntity();
      if (shouldRespondWithError) {
        entity.setContent(new ByteArrayInputStream(
            "{ \"code\" : \"error\", \"message\" : \"Failed!\" }".getBytes()));
      } else {
        entity.setContent(new ByteArrayInputStream("{ \"code\" : \"ok\" }".getBytes()));
      }
      httpResponse.setEntity(entity);
    };
  }
}
