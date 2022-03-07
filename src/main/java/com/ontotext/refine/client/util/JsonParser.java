package com.ontotext.refine.client.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Provides logic for JSON parsing.
 */
public enum JsonParser {

  JSON_PARSER;

  private static final ObjectMapper OBJECT_MAPPER =
      new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  private static final ObjectMapper STRICT_OBJECT_MAPPER =
      new ObjectMapper().disable(
          DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
          DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

  /**
   * Parses given JSON to specific type using {@link ObjectMapper}.
   *
   * @param <T> the class of the type
   * @param json the JSON that should be parsed
   * @param type the type
   * @return new instance of the given type if the parsing is successful
   * @throws IOException when there is an error during the parsing process
   */
  public <T> T read(String json, Class<T> type) throws IOException {
    return OBJECT_MAPPER.readValue(json, type);
  }

  /**
   * Parses given JSON to specific type using {@link ObjectMapper}.
   *
   * @param <T> the class of the type
   * @param json the JSON that should be parsed
   * @param type the type
   * @return new instance of the given type if the parsing is successful
   * @throws IOException when there is an error during the parsing process
   */
  public <T> T read(String json, TypeReference<T> type) throws IOException {
    return OBJECT_MAPPER.readValue(json, type);
  }

  /**
   * Parses given string to {@link JsonNode}.
   *
   * @param json to be parsed
   * @return new {@link JsonNode}
   * @throws RefineException when there is an error during the parsing process
   */
  public JsonNode parseJson(String json) throws RefineException {
    try {
      return OBJECT_MAPPER.readTree(json);
    } catch (JsonProcessingException jpe) {
      throw new RefineException("Parser error: " + jpe.getMessage(), jpe);
    }
  }

  /**
   * Parses given string to {@link JsonNode}.
   *
   * @param json to be parsed
   * @return new {@link JsonNode}
   * @throws RefineException when there is an error during the parsing process
   */
  public JsonNode parseJson(InputStream json) throws RefineException {
    try {
      return OBJECT_MAPPER.readTree(json);
    } catch (IOException jpe) {
      throw new RefineException("Parser error: " + jpe.getMessage(), jpe);
    }
  }

  /**
   * Converts given array node to list.
   *
   * @param arrayNode to be converted
   * @return new list
   */
  public List<String> toResults(JsonNode arrayNode) {
    List<String> resultList = new ArrayList<>();
    Iterator<JsonNode> iterator = arrayNode.elements();
    while (iterator.hasNext()) {
      JsonNode node = iterator.next();
      resultList.add(node.asText());
    }
    return resultList;
  }

  /**
   * Finds specific path in {@link JsonNode}.
   *
   * @param jsonNode in which the path should be searched
   * @param path to search
   * @return the found node
   * @throws RefineException when node with specified path is missing
   */
  public JsonNode findExistingPath(JsonNode jsonNode, String path) throws RefineException {
    JsonNode node = jsonNode.path(path);
    if (node.isMissingNode()) {
      throw new RefineException("Node with path '" + path + "' is missing: " + jsonNode);
    }
    return node;
  }

  /**
   * Checks whether the input JSON can be materialized into the given type.
   *
   * @param <T> the class of the type
   * @param clazz reference to the actual type
   * @return <code>true</code> if the provided JSON can be materialized to the specified class,
   *         <code>false</code> otherwise
   */
  public <T> boolean isAssignable(String json, Class<T> clazz) {
    if (StringUtils.isBlank(json) || "{}".equals(json.trim())) {
      return false;
    }

    try {
      STRICT_OBJECT_MAPPER.readValue(json, clazz);
      return true;
    } catch (IOException ioe) { // NOSONAR
      return false;
    }
  }
}
