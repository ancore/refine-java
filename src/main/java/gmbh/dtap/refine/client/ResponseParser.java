package gmbh.dtap.refine.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import static gmbh.dtap.refine.client.DeleteProjectResponse.error;
import static gmbh.dtap.refine.client.DeleteProjectResponse.ok;

/**
 * This class is shared by all response handlers to allow reuse of the unmarshaller instance.
 *
 * @since 0.1.2
 */
class ResponseParser {

   private final ObjectMapper objectMapper = new ObjectMapper();
   private final URL baseUrl;

   ResponseParser(URL baseUrl) {
      this.baseUrl = baseUrl;
   }

   DeleteProjectResponse parseDeleteProjectResponse(String json) throws IOException {
      JsonNode node = parseJson(json);
      String code = findExistingPath(node, "code").asText();
      if ("ok".equals(code)) {
         return ok();
      } else if ("error".equals(code)) {
         String message = findExistingPath(node, "message").asText();
         return error(message);
      } else {
         throw new ClientProtocolException("Unexpected code: " + code);
      }
   }

   public ProjectMetadataResponse parseAllProjectMetadataResponse(String json) throws IOException {
      JsonNode node = parseJson(json);
      JsonNode projectsNode = findExistingPath(node, "projects");
      ProjectMetadataResponse response = new ProjectMetadataResponse();
      Iterator<String> iterator = projectsNode.fieldNames();
      while (iterator.hasNext()) {
         String id = iterator.next();
         JsonNode projectNode = projectsNode.get(id);
         String name = projectNode.path("name").asText();
         URL url = new URL(baseUrl, "/project?project=" + id);
         MinimalRefineProject project = new MinimalRefineProject(id, name, url);
         response.add(project);
      }
      return response;
   }

   private JsonNode parseJson(String json) throws IOException {
      try {
         return objectMapper.readTree(json);
      } catch (JsonProcessingException e) {
         throw new ClientProtocolException("Parser error: " + e.getMessage(), e);
      }
   }

   private JsonNode findExistingPath(JsonNode jsonNode, String path) throws IOException {
      JsonNode node = jsonNode.path(path);
      if (node.isMissingNode()) {
         throw new ClientProtocolException("Node with path '" + path + "' is missing: " + jsonNode);
      }
      return node;
   }
}
