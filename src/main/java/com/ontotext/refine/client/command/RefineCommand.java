package com.ontotext.refine.client.command;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.exceptions.RefineException;
import org.apache.http.client.ResponseHandler;


/**
 * Defines the base way of command request execution and the response processing afterwards.
 *
 * @author Antoniy Kunchev
 *
 * @param <T> the type of the response
 */
public interface RefineCommand<T> extends ResponseHandler<T> {

  /**
   * Provides the command endpoint.
   *
   * @return a command endpoint
   */
  String endpoint();

  /**
   * Executes the command.
   *
   * @param client to be used for command request
   * @return a command response
   * @throws RefineException when any error occurs during command execution
   */
  T execute(RefineClient client) throws RefineException;

  /**
   * Contains constants used throughout the commands.
   *
   * @author Antoniy Kunchev
   */
  class Constants {

    public static final String PROJECT = "project";
    public static final String PROJECT_PARAM = PROJECT + "=";

    public static final String CSRF_TOKEN = "csrf_token";
    public static final String CSRF_TOKEN_PARAM = CSRF_TOKEN + "=";

    private Constants() {
      // utility
    }
  }
}
