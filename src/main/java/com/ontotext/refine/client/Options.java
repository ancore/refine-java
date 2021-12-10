package com.ontotext.refine.client;

/**
 * The options are used to provide additional information to the command and it execution.
 */
@FunctionalInterface
public interface Options {

  /**
   * Returns the options as JSON string in the format expected by the refine tool.
   *
   * @return the options as JSON string
   */
  String asJson();
}
