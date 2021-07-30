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

package com.ontotext.refine.client.command.operations;

import static com.ontotext.refine.client.JsonOperation.from;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.ResponseCode;
import com.ontotext.refine.client.command.RefineCommands;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


/**
 * Unit Tests for {@link ApplyOperationsCommand}.
 */
class ApplyOperationsCommandTest {

  private static final Charset UTF_8 = Charset.forName("UTF-8");

  @Mock
  private RefineClient refineClient;

  private ApplyOperationsCommand command;

  @BeforeEach
  void setUp() throws MalformedURLException {
    refineClient = mock(RefineClient.class);
    when(refineClient.createUrl(anyString())).thenReturn(new URL("http://localhost:3333/"));
    command = RefineCommands.applyOperations().token("test-token").project("1234567890")
        .operations(from("foo")).build();
  }

  @Test
  void should_execute() throws IOException {
    command.execute(refineClient);

    verify(refineClient).createUrl(anyString());
    verify(refineClient).execute(any(), any());
  }

  @Test
  void should_parse_apply_operation_success_response()
      throws IOException, URISyntaxException {
    String responseBody =
        IOUtils.toString(getClass().getResource("/responseBody/code-ok.json").toURI(), UTF_8);

    BasicHttpResponse httpResponse = buildHttpResponse(responseBody);
    ApplyOperationsResponse response = command.handleResponse(httpResponse);
    assertNotNull(response);
    assertEquals(ResponseCode.OK, response.getCode());
    assertNull(response.getMessage());
  }

  @Test
  void should_parse_apply_operation_error_response() throws IOException, URISyntaxException {
    String responseBody =
        IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);

    BasicHttpResponse httpResponse = buildHttpResponse(responseBody);
    ApplyOperationsResponse response = command.handleResponse(httpResponse);
    assertNotNull(response);
    assertEquals(ResponseCode.ERROR, response.getCode());
    assertEquals("This is the error message.", response.getMessage());
  }

  private BasicHttpResponse buildHttpResponse(String responseBody)
      throws UnsupportedEncodingException {
    BasicHttpResponse response =
        new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, ""));
    response.setEntity(new StringEntity(responseBody));
    return response;
  }
}