package com.ontotext.refine.client.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine.ExitCode;


/**
 * Provides common logic that could be used throughout the CLI tests.
 *
 * @author Antoniy Kunchev
 */
abstract class BaseProcessTest {

  private ByteArrayOutputStream outputOs;
  private String consoleOutputAsStr;

  private ByteArrayOutputStream errorsOs;
  private String errorsAsStr;

  private PrintStream consoleOut = System.out;
  private PrintStream consoleErr = System.err;

  /**
   * Provides the executor for the commands. Used for base tests defined in the current class. The
   * base implementation of this method should be <code>CommandClass::main</code>.
   *
   * @return the command executor
   */
  protected abstract Consumer<String[]> commandExecutor();

  @BeforeEach
  void init() {
    outputOs = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputOs));

    errorsOs = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errorsOs));
  }

  @AfterEach
  void resetToOriginal() {
    System.setOut(consoleOut);
    System.setErr(consoleErr);
  }

  @Test
  @ExpectSystemExit(ExitCode.USAGE)
  void shouldFailOnMissingRefineUri() {
    try {
      commandExecutor().accept(new String[0]);
    } finally {
      assertTrue(assertMissingArgError().contains("'--uri=<uri>'"));
    }
  }

  /**
   * Asserts that the first line of the console error output is for missing arguments and then
   * return it.
   *
   * @return the first line of the console error output
   */
  protected String assertMissingArgError() {
    String firstLine = consoleErrors().split(System.lineSeparator())[0];
    boolean condition = firstLine.contains("Missing required")
        && StringUtils.containsAny(firstLine, "options", "option", "parameters", "parameter");
    assertTrue(condition, "Expected to find missing arg line but it was <" + firstLine + ">");
    return firstLine;
  }

  /** Varargs converter. */
  protected String[] args(String... args) {
    return args;
  }

  /**
   * Provides the console output as {@link String}.
   *
   * @return the console output
   */
  protected final String consoleOutput() {
    if (consoleOutputAsStr == null) {
      consoleOutputAsStr = outputOs.toString(StandardCharsets.UTF_8);
    }
    return consoleOutputAsStr;
  }

  /**
   * Provides the console errors output as {@link String}.
   *
   * @return the console errors output
   */
  protected final String consoleErrors() {
    if (errorsAsStr == null) {
      errorsAsStr = errorsOs.toString(StandardCharsets.UTF_8);
    }
    return errorsAsStr;
  }
}
