package com.ontotext.refine.client.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ontotext.refine.client.cli.RefineResponder.HandlerContext;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HttpRequestHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import picocli.CommandLine.ExitCode;


/**
 * Test for the {@link CreateProject}.
 *
 * @author Antoniy Kunchev
 */
class CreateProjectTest extends BaseProcessTest {

  private static RefineResponder responder = new RefineResponder();
  private static boolean failCsrfRequest;

  @BeforeAll
  static void beforeAll() throws IOException {
    failCsrfRequest = false;
    responder.start(mockResponses());
  }

  @AfterAll
  static void cleanup() {
    responder.stop();
  }

  @Override
  protected Consumer<String[]> commandExecutor() {
    return CreateProject::main;
  }

  @Test
  @ExpectSystemExit(ExitCode.USAGE)
  void shouldExitWithErrorOnMissingFileArg() {
    try {
      commandExecutor().accept(args("-u " + responder.getUri()));
    } finally {
      assertTrue(assertMissingArgError().contains("FILE"));
    }
  }

  @Test
  @ExpectSystemExit(ExitCode.SOFTWARE)
  void shouldFailToGetCrsfToken() {
    try {
      failCsrfRequest = true;
      URL resource = getClass().getClassLoader().getResource("Netherlands_restaurants.csv");

      String uriArg = "-u " + responder.getUri();
      commandExecutor().accept(args(resource.getPath(), "-name Restaurants", uriArg));
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
  @ExpectSystemExit(ExitCode.OK)
  void shouldPassSuccessfully() {
    try {
      URL resource = getClass().getClassLoader().getResource("Netherlands_restaurants.csv");

      String uriArg = "-u " + responder.getUri();
      commandExecutor().accept(args(resource.getPath(), "-name Restaurants", uriArg));
    } finally {
      String errors = consoleErrors();
      assertTrue(errors.isEmpty(), "Expected no errors but there were: " + errors);

      assertEquals(
          "The project with id '1812661014997' was created successfully.",
          consoleOutput().stripTrailing());
    }
  }

  private static Map<String, HttpRequestHandler> mockResponses() {
    Map<String, HttpRequestHandler> responses = new HashMap<>(2);
    HandlerContext context = new HandlerContext().setFailCsrfRequest(() -> failCsrfRequest);
    responses.put("/orefine/command/core/get-csrf-token", RefineResponder.csrfToken(context));
    responses.put("/orefine/command/core/create-project-from-upload", createProjectHandler());
    return responses;
  }

  private static HttpRequestHandler createProjectHandler() {
    return (httpRequest, httpResponse, httpContext) -> {
      httpResponse.setStatusCode(HttpStatus.SC_MOVED_TEMPORARILY);
      httpResponse.addHeader("Location", "?projectId=1812661014997");
    };
  }
}
