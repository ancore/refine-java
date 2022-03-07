package com.ontotext.refine.client.util.mappings;

import static com.ontotext.refine.client.util.JsonParser.JSON_PARSER;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.ontotext.refine.client.command.models.GetProjectModelsCommand;
import com.ontotext.refine.client.command.operations.ApplyOperationsCommand;
import com.ontotext.refine.client.command.operations.GetOperationsCommand;
import com.ontotext.refine.client.exceptions.RefineException;
import com.ontotext.refine.client.util.mappings.MappingsStructures.MappingJson;
import com.ontotext.refine.client.util.mappings.MappingsStructures.OperationsJson;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 * Contains convenient logic for normalization of the mappings used for RDF export.
 *
 * @author Antoniy Kunchev
 */
public class MappingsNormalizer {

  private static final Predicate<JsonNode> NOT_MAPPING_ASSIGNABLE =
      json -> !JSON_PARSER.isAssignable(json.toString(), MappingJson.class);

  private MappingsNormalizer() {
    // utility
  }

  /**
   * Extracts the RDF mapping JSON from the input JSON. The input can be:<br>
   * <ul>
   * <li>operations JSON (retrieved from the OR history UI or extracted via
   * {@link GetOperationsCommand})
   * <li>models JSON (extract via {@link GetProjectModelsCommand})
   * </ul>
   * If the input is already mapping JSON, it is returned as is.
   *
   * @param json to normalize
   * @return mapping for RDF export or <code>null</code> if the provided JSON cannot be handled
   */
  public static String forRdfExport(String json) {
    if (JSON_PARSER.isAssignable(json, MappingJson.class)) {
      return json;
    }

    // operations JSON case
    if (JSON_PARSER.isAssignable(json, OperationsJson.class)) {
      return extractMappingsFromOperations(json);
    }

    // project models case
    String mappingsStr = tryExtractFromModels(json);
    if (mappingsStr != null) {
      return mappingsStr;
    }

    return null;
  }

  // extracts the last object for the RDF mapping as there could be more than one saved
  private static String extractMappingsFromOperations(String json) {
    try {
      List<JsonNode> listOfMappings = JSON_PARSER.parseJson(json).findValues("mapping");
      listOfMappings.removeIf(NOT_MAPPING_ASSIGNABLE);
      JsonNode lastMapping = listOfMappings.get(listOfMappings.size() - 1);
      return lastMapping.toString();
    } catch (RefineException re) { // NOSONAR
      return null; // shouldn't be reachable
    }
  }

  private static String tryExtractFromModels(String json) {
    if (StringUtils.isBlank(json)) {
      return null;
    }

    try {
      JsonNode models = JSON_PARSER.parseJson(json);
      JsonNode overlayModels = models.findValue("overlayModels");
      if (overlayModels == null) {
        return null;
      }

      JsonNode mappingDef = overlayModels.findValue("mappingDefinition");
      if (mappingDef == null) {
        return null;
      }

      JsonNode mappings = mappingDef.findValue("mappingDefinition");
      return mappings != null ? mappings.toString() : null;
    } catch (RefineException re) { // NOSONAR
      return null;
    }
  }

  /**
   * Checks, whether the input JSON is mapping for RDF export and if it is, wraps it as operation so
   * that it can be passed to the {@link ApplyOperationsCommand}.<br>
   * Otherwise the input argument is returned as is.
   *
   * @param json to try to convert to mapping operation
   * @return mappings wrapped as operation or the input argument
   */
  public static String forApplyOperations(String json) {
    if (!JSON_PARSER.isAssignable(json, MappingJson.class)) {
      return json;
    }

    try {
      return JsonNodeFactory.instance.objectNode()
          .put("op", "mapping-editor/save-rdf-mapping")
          .set("mapping", JSON_PARSER.parseJson(json))
          .toString();
    } catch (RefineException re) { // NOSONAR
      return null; // shouldn't be reachable
    }
  }
}
