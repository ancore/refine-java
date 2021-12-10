package com.ontotext.refine.client;

/**
 * A minimal implementation of {@link Operation}.
 */
public class JsonOperation implements Operation {

  private final String json;

  private JsonOperation(String json) {
    this.json = json;
  }

  /**
   * Factory method that crates an instance from JSON_PARSER. The JSON_PARSER has to be in the
   * format expected by OpenRefine.
   *
   * @param json the JSON_PARSER document
   * @return the engine instance
   */
  public static Operation from(String json) {
    return new JsonOperation(json);
  }

  @Override
  public String asJson() {
    return json;
  }

  @Override
  public String toString() {
    return asJson();
  }
}
