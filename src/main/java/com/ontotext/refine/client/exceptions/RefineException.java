package com.ontotext.refine.client.exceptions;

import java.io.IOException;


/**
 * Thrown when the server responds with an error or unexpected response, and when the response can
 * not be understood.
 */
public class RefineException extends IOException {

  private static final long serialVersionUID = 5368187574797784277L;

  public RefineException(String message) {
    super(message);
  }

  public RefineException(String template, Object... args) {
    super(String.format(template, args));
  }

  public RefineException(String message, Throwable cause) {
    super(message, cause);
  }
}
