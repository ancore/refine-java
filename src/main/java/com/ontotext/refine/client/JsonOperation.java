/*
 * Copyright 2019 DTAP GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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
