package com.ontotext.refine.client.command.delete;

import static org.apache.http.util.Asserts.notEmpty;

import com.ontotext.refine.client.RefineResponse;
import com.ontotext.refine.client.ResponseCode;

/**
 * This class represents the response from the <code>delete project</code> request.
 */
public class DeleteProjectResponse extends RefineResponse {

  /**
   * Private constructor to enforce usage of factory methods.
   *
   * @param code the code
   * @param message the message, may be {@code null}
   */
  private DeleteProjectResponse(ResponseCode code, String message) {
    super(code, message);
  }

  /**
   * Returns an instance to represent the success status.
   *
   * @return the success instance
   */
  static DeleteProjectResponse ok() {
    return new DeleteProjectResponse(ResponseCode.OK, null);
  }

  /**
   * Returns an instance to represent an error.
   *
   * @param message the error message
   * @return the error instance
   */
  static DeleteProjectResponse error(String message) {
    notEmpty(message, "The 'message' should not be empty.");
    return new DeleteProjectResponse(ResponseCode.ERROR, message);
  }
}
