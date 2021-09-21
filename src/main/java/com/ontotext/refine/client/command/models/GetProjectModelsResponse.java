package com.ontotext.refine.client.command.models;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Holds the response from {@link GetProjectModelsCommand}.
 *
 * @author Antoniy Kunchev
 */
public class GetProjectModelsResponse {

  private JsonNode columnModel;
  private JsonNode httpHeaders;
  private JsonNode overlayModels;
  private JsonNode recordModel;
  private JsonNode scripting;

  public JsonNode getColumnModel() {
    return columnModel;
  }

  public void setColumnModel(JsonNode columnModel) {
    this.columnModel = columnModel;
  }

  public JsonNode getHttpHeaders() {
    return httpHeaders;
  }

  public void setHttpHeaders(JsonNode httpHeaders) {
    this.httpHeaders = httpHeaders;
  }

  public JsonNode getOverlayModels() {
    return overlayModels;
  }

  public void setOverlayModels(JsonNode overlayModels) {
    this.overlayModels = overlayModels;
  }

  public JsonNode getRecordModel() {
    return recordModel;
  }

  public void setRecordModel(JsonNode recordModel) {
    this.recordModel = recordModel;
  }

  public JsonNode getScripting() {
    return scripting;
  }

  public void setScripting(JsonNode scripting) {
    this.scripting = scripting;
  }
}
