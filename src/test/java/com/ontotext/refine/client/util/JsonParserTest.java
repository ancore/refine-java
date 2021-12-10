package com.ontotext.refine.client.util;

import static com.ontotext.refine.client.util.JsonParser.JSON_PARSER;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;


/**
 * Unit Tests for {@link JsonParser}.
 */
class JsonParserTest {

  private static final Charset UTF_8 = Charset.forName("UTF-8");

  @Test
  void should_have_instance() {
    // -_- ??

    assertNotNull(JSON_PARSER);
  }

  @Test
  void should_throw_exception_when_not_parsable_as_json()
      throws IOException, URISyntaxException {
    String plainText =
        IOUtils.toString(getClass().getResource("/responseBody/plain.txt").toURI(), UTF_8);

    assertThrows(RefineException.class, () -> JSON_PARSER.parseJson(plainText));
  }
}
