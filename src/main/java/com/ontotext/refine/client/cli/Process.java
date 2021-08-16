package com.ontotext.refine.client.cli;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.RefineClients;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.command.csrf.GetCsrfTokenCommand;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.concurrent.Callable;
import org.apache.commons.io.IOUtils;
import picocli.CommandLine.Option;


/**
 * Base abstraction for all CLI processes. It defines common arguments for all processes and
 * provides some convenient methods used in the processes.
 *
 * @author Antoniy Kunchev
 */
abstract class Process implements Callable<Integer> {

  // cached instance to avoid re-initialization, when there are multiple request within one process
  private RefineClient client;

  @Option(
      names = {"-u", "--uri"},
      description = "The URI to the OntoRefine instance."
          + " It should include the protocol, host, port and context path if needed.",
      required = true)
  private String uri;

  /**
   * Creates {@link RefineClient} instance and caches it in order to be reused if there are
   * subsequent uses within the same process.
   *
   * @return a {@link RefineClient} instance
   * @throws RefineException when there is an issue with the creation of the client
   */
  protected RefineClient getClient() throws RefineException {
    if (client == null) {
      try {
        client = RefineClients.create(uri);
      } catch (Exception exc) {
        throw new RefineException("Failed to get client instance due to: " + exc.getMessage());
      }
    }
    return client;
  }

  /**
   * Executes {@link GetCsrfTokenCommand} and returns the result token.
   *
   * @return a CSRF token as string
   * @throws RefineClientException when the retrieval of the token fails
   */
  protected String getToken() throws RefineException {
    try {
      return RefineCommands.getCsrfToken().build().execute(getClient()).getToken();
    } catch (RefineException re) {
      throw re;
    } catch (Exception exc) {
      throw new RefineException("Failed to retrieve CSRF token due to: " + exc.getMessage());
    }
  }

  /**
   * Writes the source {@link InputStream} into the destination {@link File}. The method will use
   * different {@link PrintStream} to write the file and restore the original console
   * {@link PrintStream} instance. The method take cares for the closing of the streams.
   *
   * @param source the stream that should be written
   * @param destination the file where the data should be written
   * @throws RefineException when error occurs during the writing process
   */
  protected void writeOutAsFile(InputStream source, File destination) throws RefineException {
    PrintStream consolePrinter = System.out;
    try (PrintStream ps = new PrintStream(destination); InputStream is = source) {
      System.setOut(ps);
      IOUtils.copyLarge(is, ps);
    } catch (IOException ioe) {
      throw new RefineException(
          "Failed to write '%s' file due to: %s", destination.getName(), ioe.getMessage());
    } finally {
      System.setOut(consolePrinter);
    }
  }
}
