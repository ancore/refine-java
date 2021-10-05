package com.ontotext.refine.client.command.operations;

import com.fasterxml.jackson.databind.JsonNode;


/**
 * Holds the response from {@link GetOperationsCommand}.
 *
 * @author Antoniy Kunchev
 */
public class GetOperationsResponse {

  private String project;
  private JsonNode content;

  public String getProject() {
    return project;
  }

  GetOperationsResponse setProject(String project) {
    this.project = project;
    return this;
  }

  public JsonNode getContent() {
    return content;
  }

  GetOperationsResponse setContent(JsonNode content) {
    this.content = content;
    return this;
  }
}
