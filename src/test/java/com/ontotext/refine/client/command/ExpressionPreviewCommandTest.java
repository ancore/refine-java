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

package com.ontotext.refine.client.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.ResponseCode;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Unit Tests for {@link ExpressionPreviewCommand}.
 */
class ExpressionPreviewCommandTest {

  private static final Charset UTF_8 = Charset.forName("UTF-8");

  @Mock
  private RefineClient refineClient;

  private ExpressionPreviewCommand command;

  @BeforeEach
  void setUp() throws MalformedURLException {
    MockitoAnnotations.openMocks(this);

    when(refineClient.createUrl(anyString())).thenReturn(new URL("http://localhost:3333/"));
    command = RefineCommands
        .expressionPreview()
        .token("test-token")
        .project("1234567890")
        .rowIndices(0)
        .expression("foo")
        .build();
  }

  @Test
  void should_execute() throws IOException {
    command.execute(refineClient);
    verify(refineClient).createUrl(anyString());
    verify(refineClient).execute(any(), any());
  }

  @Test
  void should_parse_expression_preview_success_response()
      throws IOException, URISyntaxException {
    String responseBody = IOUtils
        .toString(getClass().getResource("/responseBody/expression-preview.json").toURI(), UTF_8);

    ExpressionPreviewResponse response = command.parseExpressionPreviewResponse(responseBody);
    assertNotNull(response);
    assertEquals(ResponseCode.OK, response.getCode());
    assertNull(response.getMessage());
    assertEquals(List.of("7", "5", "5", "9"), response.getExpressionPreviews());
  }

  @Test
  void should_parse_expression_preview_error_response()
      throws IOException, URISyntaxException {
    String responseBody =
        IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);

    ExpressionPreviewResponse response = command.parseExpressionPreviewResponse(responseBody);
    assertNotNull(response);
    assertEquals(ResponseCode.ERROR, response.getCode());
    assertEquals("This is the error message.", response.getMessage());
  }
}
