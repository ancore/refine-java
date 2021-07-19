package com.ontotext.refine.client.command.reconcile;

import static org.apache.http.util.Asserts.notBlank;

import com.ontotext.refine.client.RefineResponse;
import com.ontotext.refine.client.ResponseCode;


/**
 * Holds the response from {@link ReconcileCommand}.
 *
 * @author Antoniy Kunchev
 */
public class ReconcileCommandResponse extends RefineResponse {

  private ReconcileCommandResponse(ResponseCode code, String message) {
    super(code, message);
  }

  /**
   * Returns an instance to represent the success status.
   *
   * @return the success instance
   */
  static ReconcileCommandResponse ok() {
    return new ReconcileCommandResponse(ResponseCode.OK, null);
  }


  /**
   * Returns an instance to represent the pending status.
   *
   * @return the pending instance
   */
  static ReconcileCommandResponse pending() {
    return new ReconcileCommandResponse(ResponseCode.PENDING, null);
  }

  /**
   * Returns an instance to represent an error.
   *
   * @param message the error message
   * @return the error instance
   */
  static ReconcileCommandResponse error(String message) {
    notBlank(message, "The message should not be empty.");
    return new ReconcileCommandResponse(ResponseCode.ERROR, message);
  }
}
