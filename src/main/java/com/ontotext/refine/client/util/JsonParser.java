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

package com.ontotext.refine.client.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontotext.refine.client.RefineException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public enum JsonParser {

  JSON_PARSER;

  private final ObjectMapper objectMapper =
      new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;

  public <T> T read(String json, Class<T> type) throws IOException {
    T t = objectMapper.readValue(json, type);
    return type.cast(t);
  }

  public JsonNode parseJson(String json) throws IOException {
    try {
      return objectMapper.readTree(json);
    } catch (JsonProcessingException e) {
      throw new RefineException("Parser error: " + e.getMessage(), e);
    }
  }

  public List<String> toResults(JsonNode arrayNode) {
    List<String> resultList = new ArrayList<>();
    Iterator<JsonNode> iterator = arrayNode.elements();
    while (iterator.hasNext()) {
      JsonNode node = iterator.next();
      resultList.add(node.asText());
    }
    return resultList;
  }

  public JsonNode findExistingPath(JsonNode jsonNode, String path) throws IOException {
    JsonNode node = jsonNode.path(path);
    if (node.isMissingNode()) {
      throw new RefineException("Node with path '" + path + "' is missing: " + jsonNode);
    }
    return node;
  }
}
