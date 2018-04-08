package gmbh.dtap.refine.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gmbh.dtap.refine.api.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static java.time.OffsetDateTime.parse;

/**
 * This class is shared by all response handlers to allow reuse of the unmarshaller instance.
 * <p>
 * All JSON parsing of OpenRefine responses is done here.
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
         return DeleteProjectResponse.ok();
      } else if ("error".equals(code)) {
         String message = findExistingPath(node, "message").asText();
         return DeleteProjectResponse.error(message);
      } else {
         throw new RefineException("Unexpected code: " + code);
      }
   }

   ProjectMetadataResponse parseAllProjectMetadataResponse(String json) throws IOException {
      JsonNode node = parseJson(json);
      JsonNode projectsNode = node.path("projects");
      if (projectsNode.isMissingNode()) {
         // successful, but no projects available
         return new ProjectMetadataResponse();
      }
      ProjectMetadataResponse response = new ProjectMetadataResponse();
      Iterator<String> iterator = projectsNode.fieldNames();
      while (iterator.hasNext()) {
         String id = iterator.next();
         JsonNode projectNode = projectsNode.get(id);
         response.add(toRefineProject(id, projectNode));
      }
      return response;
   }

   private RefineProject toRefineProject(String id, JsonNode projectNode) throws IOException {
      URL url = new URL(baseUrl, "/project?project=" + id);
      String name = findExistingPath(projectNode, "name").asText();
      RefineProjectLocation location = MinimalRefineProjectLocation.from(url);
      MinimalRefineProject project = new MinimalRefineProject(name, location);
      project.setCreated(parse(findExistingPath(projectNode, "created").asText()));
      project.setModified(parse(findExistingPath(projectNode, "modified").asText()));
      project.setCreator(findExistingPath(projectNode, "creator").asText());
      project.setContributors(findExistingPath(projectNode, "contributors").asText());
      project.setSubject(findExistingPath(projectNode, "subject").asText());
      project.setDescription(findExistingPath(projectNode, "description").asText());
      project.setRowCount(findExistingPath(projectNode, "rowCount").asLong());
      project.setCustomMetadata(toCustomMetadata(findExistingPath(projectNode, "customMetadata")));
      JsonNode importOptionMetadataNode = projectNode.path("importOptionMetadata");
      if (importOptionMetadataNode.isMissingNode()) {
         project.setImportOptionMetadata(Collections.emptyList());
      } else {
         if (importOptionMetadataNode.isArray()) {
            project.setImportOptionMetadata(toImportOptionMetadata(importOptionMetadataNode));
         } else {
            throw new RefineException("Node with path '\" + path + \"' is not any array: " + importOptionMetadataNode);
         }
      }
      return project;
   }

   private CustomMetadata toCustomMetadata(JsonNode jsonNode) {
      // TODO: https://github.com/dtap-gmbh/refine-java/issues/2
      return new MinimalCustomMetadata();
   }

   private List<ImportOptionMetadata> toImportOptionMetadata(JsonNode arrayNode) throws IOException {
      List<ImportOptionMetadata> metadataList = new ArrayList<>();
      Iterator<JsonNode> iterator = arrayNode.elements();
      while (iterator.hasNext()) {
         JsonNode node = iterator.next();
         MinimalImportOptionMetadata metadata = new MinimalImportOptionMetadata();
         metadata.setStoreBlankRows(findExistingPath(node, "storeBlankRows").asBoolean());
         metadata.setIncludeFileSources(findExistingPath(node, "includeFileSources").asBoolean());
         metadata.setSkipDataLines(findExistingPath(node, "skipDataLines").asInt());
         metadata.setGuessCellValueType(findExistingPath(node, "guessCellValueTypes").asBoolean());
         metadata.setHeaderLines(findExistingPath(node, "headerLines").asInt());
         metadata.setIgnoreLines(findExistingPath(node, "ignoreLines").asInt());
         metadata.setProcessQuotes(findExistingPath(node, "processQuotes").asBoolean());
         metadata.setFileSource(findExistingPath(node, "fileSource").asText());
         metadata.setProjectName(findExistingPath(node, "projectName").asText());
         metadata.setSeparator(findExistingPath(node, "separator").asText());
         metadata.setStoreBlankCellsAsNulls(findExistingPath(node, "storeBlankCellsAsNulls").asBoolean());
         metadataList.add(metadata);
      }
      return metadataList;
   }


   ApplyOperationsResponse parseApplyOperationsResponse(String json) throws IOException {
      JsonNode node = parseJson(json);
      String code = findExistingPath(node, "code").asText();
      if ("ok".equals(code)) {
         return ApplyOperationsResponse.ok();
      } else if ("error".equals(code)) {
         String message = findExistingPath(node, "message").asText();
         return ApplyOperationsResponse.error(message);
      } else {
         throw new RefineException("Unexpected code: " + code);
      }

   }

   private JsonNode parseJson(String json) throws IOException {
      try {
         return objectMapper.readTree(json);
      } catch (JsonProcessingException e) {
         throw new RefineException("Parser error: " + e.getMessage(), e);
      }
   }

   private JsonNode findExistingPath(JsonNode jsonNode, String path) throws IOException {
      JsonNode node = jsonNode.path(path);
      if (node.isMissingNode()) {
         throw new RefineException("Node with path '" + path + "' is missing: " + jsonNode);
      }
      return node;
   }
}
