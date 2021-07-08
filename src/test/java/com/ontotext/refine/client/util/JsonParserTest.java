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

import static com.ontotext.refine.client.util.JsonParser.JSON_PARSER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ontotext.refine.client.RefineException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

/**
 * Unit Tests for {@link JsonParser}.
 */
public class JsonParserTest {

  private static final Charset UTF_8 = Charset.forName("UTF-8");

  @Test
  public void should_have_instance() {
    // -_- ??

    assertNotNull(JSON_PARSER);
  }

  @Test
  public void should_have_static_instance() {
    // -_- ??

    assertEquals(JSON_PARSER, JSON_PARSER);
  }

  @Test
  public void should_throw_exception_when_not_parsable_as_json()
      throws IOException, URISyntaxException {
    String plainText =
        IOUtils.toString(getClass().getResource("/responseBody/plain.txt").toURI(), UTF_8);

    assertThrows(RefineException.class, () -> JSON_PARSER.parseJson(plainText));
  }
}
