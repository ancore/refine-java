package com.ontotext.refine.client.command.export;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * Provides a engine options for the {@link ExportRowsCommand}.
 *
 * @author Antoniy Kunchev
 */
public enum Engines {

  ROW_BASED(JsonNodeFactory.instance.objectNode().put("mode", "row-based"));

  private final JsonNode engine;

  private Engines(final JsonNode engine) {
    this.engine = engine;
  }

  /**
   * Provides a string representation of the engine configuration.
   *
   * @return the string representation of the JSON that is the used for engine configuration of the
   *         refine tool
   */
  public String get() {
    return engine.asText();
  }
}
