package com.ontotext.refine.client.command.reconcile;

import com.ontotext.refine.client.RefineResponse;
import com.ontotext.refine.client.ResponseCode;
import org.apache.commons.lang3.Validate;


/**
 * Holds the response from {@link ReconServiceRegistrationCommand}.
 *
 * @author Antoniy Kunchev
 */
public class ReconServiceRegistrationCommandResponse extends RefineResponse {

  private ReconServiceRegistrationCommandResponse(ResponseCode code, String message) {
    super(code, message);
  }

  /**
   * Returns an instance to represent the success status.
   *
   * @return the success instance
   */
  static ReconServiceRegistrationCommandResponse ok() {
    return new ReconServiceRegistrationCommandResponse(ResponseCode.OK, null);
  }

  /**
   * Returns an instance to represent an error.
   *
   * @param message the error message
   * @return the error instance
   */
  static ReconServiceRegistrationCommandResponse error(String message) {
    Validate.notBlank(message, "The message should not be empty.");
    return new ReconServiceRegistrationCommandResponse(ResponseCode.ERROR, message);
  }
}
