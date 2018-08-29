package gmbh.dtap.refine.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

enum JsonParser {

   JSON_PARSER;

   private final ObjectMapper objectMapper = new ObjectMapper();

   ApplyOperationsResponse parseApplyOperationsResponse(String json) throws IOException {
      JsonNode node = parseJson(json);
      String code = findExistingPath(node, "code").asText();
      if ("ok".equals(code)) {
         return ApplyOperationsResponse.ok();
      } else if ("pending".equals(code)) {
         return ApplyOperationsResponse.pending();
      } else if ("error".equals(code)) {
         String message = findExistingPath(node, "message").asText();
         return ApplyOperationsResponse.error(message);
      } else {
         throw new RefineException("Unexpected code: " + code);
      }
   }

   DeleteProjectResponse parseDeleteProjectResponse(String json) throws IOException {
      JsonNode node = parseJson(json);
      String code = findExistingPath(node, "code").asText();
      if ("ok".equals(code)) {
         return DeleteProjectResponse.ok();
      } else if ("error".equals(code)) {
         String message = findExistingPath(node, "message").asText();
         return DeleteProjectResponse.error(message);
      } else {
         throw new RefineException("Unexpected code: " + code);
      }
   }

   GetVersionResponse parseGetVersionResponse(String json) throws IOException {
      JsonNode node = parseJson(json);
      return new GetVersionResponse(
            findExistingPath(node, "full_name").asText(),
            findExistingPath(node, "full_version").asText(),
            findExistingPath(node, "version").asText(),
            findExistingPath(node, "revision").asText());
   }

   ExpressionPreviewResponse parseExpressionPreviewResponse(String json) throws IOException {
      JsonNode node = parseJson(json);
      String code = findExistingPath(node, "code").asText();
      if ("ok".equals(code)) {
         JsonNode resultsNode = findExistingPath(node, "results");
         if (resultsNode.isMissingNode()) {
            return ExpressionPreviewResponse.ok(Collections.emptyList());
         }
         if (resultsNode.isArray()) {
            List<String> results = toResults(resultsNode);
            return ExpressionPreviewResponse.ok(results);
         } else {
            throw new RefineException("Node with path 'results' is not any array: " + resultsNode);
         }
      } else if ("error".equals(code)) {
         String message = findExistingPath(node, "message").asText();
         return ExpressionPreviewResponse.error(message);
      } else {
         throw new RefineException("Unexpected code: " + code);
      }
   }

   JsonNode parseJson(String json) throws IOException {
      try {
         return objectMapper.readTree(json);
      } catch (JsonProcessingException e) {
         throw new RefineException("Parser error: " + e.getMessage(), e);
      }
   }

   private List<String> toResults(JsonNode arrayNode) {
      List<String> resultList = new ArrayList<>();
      Iterator<JsonNode> iterator = arrayNode.elements();
      while (iterator.hasNext()) {
         JsonNode node = iterator.next();
         resultList.add(node.asText());
      }
      return resultList;
   }

   private JsonNode findExistingPath(JsonNode jsonNode, String path) throws IOException {
      JsonNode node = jsonNode.path(path);
      if (node.isMissingNode()) {
         throw new RefineException("Node with path '" + path + "' is missing: " + jsonNode);
      }
      return node;
   }
}
