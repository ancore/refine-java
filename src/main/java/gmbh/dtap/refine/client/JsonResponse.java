package gmbh.dtap.refine.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * This class parses a JSON response document from the refine server and provides convenience methods to access fields.
 *
 * @since 0.1.0
 */
public class JsonResponse {

   private static final ObjectMapper objectMapper = new ObjectMapper();
   private final JsonNode jsonNode;

   private JsonResponse(JsonNode jsonNode) {
      this.jsonNode = jsonNode;
   }

   public static JsonResponse from(String json) throws IOException {
      JsonNode jsonNode = objectMapper.readTree(json);
      return new JsonResponse(jsonNode);
   }

   public boolean isSuccessful() {
      return "ok".equals(getCode());
   }

   public boolean isErroneous() {
      return "error".equals(getCode());
   }

   public String getCode() {
      return jsonNode.get("code").asText();
   }

   public String getErrorMessage() {
      return jsonNode.get("message").asText();
   }

   @Override
   public String toString() {
      return jsonNode.toString();
   }
}
