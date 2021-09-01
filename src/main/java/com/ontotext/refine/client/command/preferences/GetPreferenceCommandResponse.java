package com.ontotext.refine.client.command.preferences;

import com.fasterxml.jackson.databind.JsonNode;


/**
 * Holds the response from the {@link GetPreferenceCommand}.
 *
 * @author Antoniy Kunchev
 */
public class GetPreferenceCommandResponse {

  private final JsonNode result;

  GetPreferenceCommandResponse(JsonNode result) {
    this.result = result;
  }

  public JsonNode getResult() {
    return result;
  }
}
