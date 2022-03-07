package com.ontotext.refine.client.util.mappings;

import static com.ontotext.refine.client.util.JsonParser.JSON_PARSER;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.JsonNode;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test for {@link MappingsNormalizer}.
 *
 * @author Antoniy Kunchev
 */
class MappingsNormalizerTest {

  private static final JsonNode EXPECTED_FOR_RDF_EXPORT =
      toJson(loadResource("expected/forRdfExport_mappings.json"));

  private static final JsonNode EXPECTED_FOR_APPLY_OPERATIONS =
      toJson(loadResource("expected/forApplyOperations_mappings.json"));

  @ParameterizedTest
  @ValueSource(strings = {
      "forRdfExport_mappings.json",
      "forRdfExport_operations.json",
      "forRdfExport_project-models.json"})
  void forRdfExport(String resource) {
    String mappings = loadResource(resource);

    String actual = MappingsNormalizer.forRdfExport(mappings);

    assertEquals(EXPECTED_FOR_RDF_EXPORT, toJson(actual));
  }

  @Test
  void forRdfExport_emptyJson() {
    assertNull(MappingsNormalizer.forRdfExport("{}"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void forRdfExport_emptyOrNullArg(String input) {
    assertNull(MappingsNormalizer.forRdfExport(input));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void forApplyOperations_emptyOrNullArg(String input) {
    assertEquals(input, MappingsNormalizer.forApplyOperations(input));
  }

  @Test
  void forApplyOperations_projectModelsJson() {
    String mappings = loadResource("forApplyOperations_mapping.json");

    String actual = MappingsNormalizer.forApplyOperations(mappings);

    assertEquals(EXPECTED_FOR_APPLY_OPERATIONS, toJson(actual));
  }

  private static JsonNode toJson(String value) {
    try {
      return JSON_PARSER.parseJson(value);
    } catch (RefineException re) {
      fail("Failed to convert value to JSON.", re);
      return null;
    }
  }

  private static String loadResource(String resource)  {
    try {
      return IOUtils.toString(
          MappingsNormalizerTest.class.getClassLoader()
              .getResourceAsStream("mappings-normalizer/" + resource),
          StandardCharsets.UTF_8);
    } catch (IOException ioe) {
      fail("Failed to load resource: " + resource, ioe);
      return null;
    }
  }
}
