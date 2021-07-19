package com.ontotext.refine.client.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ontotext.refine.client.cli.RefineResponder.HandlerContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.protocol.HttpRequestHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import picocli.CommandLine.ExitCode;


/**
 * Test for {@link Reconcile}.
 *
 * @author Antoniy Kunchev
 */
class ReconcileTest extends BaseProcessTest {

  private static final String PROJECT = "1812661014997";
  private static final String COLUMN = "City";
  private static final String SERVICE = "https://wikidata.reconci.link/en/api";
  private static final String ASYNC = "-a";

  private static RefineResponder responder = new RefineResponder();
  private static boolean shouldReturnEmptyColumnTypes;
  private static boolean shouldReturnErrorForColumnTypes;
  private static boolean shouldReturnEmptyProcesses;
  private static boolean shouldReturnIrrelevantProcess;
  private static boolean shouldReturnErrorForReconcile;

  @BeforeAll
  static void beforeAll() throws IOException {
    shouldReturnEmptyColumnTypes = false;
    shouldReturnErrorForColumnTypes = false;
    shouldReturnEmptyProcesses = false;
    shouldReturnIrrelevantProcess = false;
    shouldReturnErrorForReconcile = false;
    responder.start(mockResponses());
  }

  @AfterAll
  static void cleanup() {
    responder.stop();
  }

  @Override
  protected Consumer<String[]> commandExecutor() {
    return Reconcile::main;
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
  @ExpectSystemExit(ExitCode.USAGE)
  void shouldExitWithErrorOnMissingColumnArg() {
    try {
      commandExecutor().accept(args(PROJECT, "-u " + responder.getUri()));
    } finally {
      assertTrue(assertMissingArgError().contains("COLUMN"));
    }
  }

  @Test
  @ExpectSystemExit(ExitCode.USAGE)
  void shouldExitWithErrorOnMissingServiceArg() {
    try {
      commandExecutor().accept(args(PROJECT, COLUMN, "-u " + responder.getUri()));
    } finally {
      assertTrue(assertMissingArgError().contains("SERVICE"));
    }
  }

  @Test
  @ExpectSystemExit(ExitCode.OK)
  void shouldPassSuccessfullyAsyncExecution() {
    try {
      commandExecutor().accept(args(PROJECT, COLUMN, SERVICE, ASYNC, "-u " + responder.getUri()));
    } finally {
      String errors = consoleErrors();
      assertTrue(errors.isEmpty(), "Expected no errors but there were: " + errors);

      String[] outputArray = consoleOutput().split(System.lineSeparator());
      String lastLine = outputArray[outputArray.length - 1];

      assertEquals(
          "The reconciliation process for column '" + COLUMN
              + "' is currently running in asynchronous mode.",
          lastLine);
    }
  }

  @Test
  @ExpectSystemExit(ExitCode.OK)
  void shouldPassSuccessfullyButFailedToTrackProgress() {
    try {
      shouldReturnIrrelevantProcess = true;
      commandExecutor().accept(args(PROJECT, COLUMN, SERVICE, "-u " + responder.getUri()));
    } finally {
      shouldReturnIrrelevantProcess = false;

      String[] outputArray = consoleOutput().split(System.lineSeparator());
      String lastLine = outputArray[outputArray.length - 1];

      assertEquals(
          "Can't track the progress of the reconciliation process. "
              + "The process is most likely still running in background.",
          lastLine);
    }
  }

  /**
   * Takes up to 2-3 seconds for execution as the functionality under testing has built-in timeout.
   */
  @Test
  @ExpectSystemExit(ExitCode.OK)
  void shouldPassSuccessfullyWithProgress() {
    try {
      new Timer().schedule(changeFlag(), 1000); // simulates completion of the process after time
      commandExecutor().accept(args(PROJECT, COLUMN, SERVICE, "-u " + responder.getUri()));
    } finally {
      shouldReturnEmptyProcesses = false; // reset flag

      String errors = consoleErrors();
      assertTrue(errors.isEmpty(), "Expected no errors but there were: " + errors);

      String[] outputArray = consoleOutput().split(System.lineSeparator());

      assertEquals("Guessing the type of the column. It may take a while...", outputArray[0]);
      assertEquals("Done.", outputArray[2]);
    }
  }

  private TimerTask changeFlag() {
    return new TimerTask() {
      @Override
      public void run() {
        shouldReturnEmptyProcesses = true;
      }
    };
  }

  @Test
  @ExpectSystemExit(ExitCode.SOFTWARE)
  void shouldFailToRetrieveColumnTypes() {
    try {
      shouldReturnEmptyColumnTypes = true;
      commandExecutor().accept(args(PROJECT, COLUMN, SERVICE, "-u " + responder.getUri()));
    } finally {
      shouldReturnEmptyColumnTypes = false;

      String expected = String.format(
          "Failed to guess the type of the column: '%s' for project: '%s'", COLUMN, PROJECT);
      assertEquals(expected, consoleErrors().stripTrailing());
    }
  }

  @Test
  @ExpectSystemExit(ExitCode.SOFTWARE)
  void shouldFailOnColumnTypesErrorResponse() {
    try {
      shouldReturnErrorForColumnTypes = true;
      commandExecutor().accept(args(PROJECT, COLUMN, SERVICE, "-u " + responder.getUri()));
    } finally {
      shouldReturnErrorForColumnTypes = false;

      String expected = String.format(
          "Failed to guess the type of the column: '%s' for project: '%s'"
              + " due to: Guess types error.",
          COLUMN,
          PROJECT);
      assertEquals(expected, consoleErrors().stripTrailing());
    }
  }

  @Test
  @ExpectSystemExit(ExitCode.SOFTWARE)
  void shouldFailOnReconcileErrorResponse() {
    try {
      shouldReturnErrorForReconcile = true;
      commandExecutor().accept(args(PROJECT, COLUMN, SERVICE, "-u " + responder.getUri()));
    } finally {
      shouldReturnErrorForReconcile = false;

      String expected = String.format(
          "Failed to reconcile column '%s' for project '%s' due to: Reconcile error.",
          COLUMN,
          PROJECT);
      assertEquals(expected, consoleErrors().stripTrailing());
    }
  }

  private static Map<String, HttpRequestHandler> mockResponses() {
    Map<String, HttpRequestHandler> handlers = new HashMap<>();
    HandlerContext context = new HandlerContext();
    handlers.put("/orefine/command/core/get-csrf-token", RefineResponder.csrfToken(context));
    handlers.put("/orefine/command/core/guess-types-of-column", guessTypesOfColumnHandler());
    handlers.put("/orefine/command/core/reconcile", reconcileHandler());
    handlers.put("/orefine/command/core/get-processes", getProcessesHandler());
    return handlers;
  }

  private static HttpRequestHandler guessTypesOfColumnHandler() {
    return (request, response, context) -> {
      response.setStatusCode(HttpStatus.SC_OK);
      BasicHttpEntity entity = new BasicHttpEntity();
      if (shouldReturnEmptyColumnTypes) {
        entity.setContent(new ByteArrayInputStream("{\"code\":\"ok\", \"types\":[]}".getBytes()));
      } else if (shouldReturnErrorForColumnTypes) {
        entity.setContent(new ByteArrayInputStream(
            "{\"code\":\"error\", \"message\":\"Guess types error.\"}".getBytes()));
      } else {
        entity.setContent(loadFile("city-column-types.json"));
      }
      response.setEntity(entity);
    };
  }

  private static HttpRequestHandler reconcileHandler() {
    return (request, response, context) -> {
      response.setStatusCode(HttpStatus.SC_OK);
      BasicHttpEntity entity = new BasicHttpEntity();
      if (shouldReturnErrorForReconcile) {
        entity.setContent(new ByteArrayInputStream(
            "{\"code\":\"error\", \"message\":\"Reconcile error.\"}".getBytes()));
      } else {
        entity.setContent(new ByteArrayInputStream("{\"code\":\"pending\"}".getBytes()));
      }
      response.setEntity(entity);
    };
  }

  private static HttpRequestHandler getProcessesHandler() {
    return (request, response, context) -> {
      response.setStatusCode(HttpStatus.SC_OK);
      BasicHttpEntity entity = new BasicHttpEntity();
      if (shouldReturnEmptyProcesses) {
        entity.setContent(new ByteArrayInputStream("{\"processes\":[]}".getBytes()));
      } else if (shouldReturnIrrelevantProcess) {
        entity.setContent(loadFile("get-processes-irrelevant.json"));
      } else {
        entity.setContent(loadFile("get-processes.json"));
      }
      response.setEntity(entity);
    };
  }

  private static InputStream loadFile(String file) {
    return Reconcile.class.getClassLoader().getResourceAsStream(file);
  }
}
