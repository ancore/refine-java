package com.ontotext.refine.client.command.preferences;

import com.ontotext.refine.client.RefineResponse;
import com.ontotext.refine.client.ResponseCode;
import org.apache.commons.lang3.Validate;


/**
 * Holds the response from {@link SetPreferenceCommand}.
 * 
 * @author Antoniy Kunchev
 */
public class SetPreferenceCommandResponse extends RefineResponse {

  private SetPreferenceCommandResponse(ResponseCode code, String message) {
    super(code, message);
  }

  /**
   * Returns an instance to represent the success status.
   *
   * @return the success instance
   */
  static SetPreferenceCommandResponse ok() {
    return new SetPreferenceCommandResponse(ResponseCode.OK, null);
  }

  /**
   * Returns an instance to represent an error.
   *
   * @param message the error message
   * @return the error instance
   */
  static SetPreferenceCommandResponse error(String message) {
    Validate.notBlank(message, "The message should not be empty.");
    return new SetPreferenceCommandResponse(ResponseCode.ERROR, message);
  }
}
