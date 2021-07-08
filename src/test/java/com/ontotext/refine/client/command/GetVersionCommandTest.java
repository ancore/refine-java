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

import static com.ontotext.refine.client.testsupport.HttpMock.mockHttpResponse;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.RefineException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * Unit Tests for {@link GetVersionCommand}.
 */
public class GetVersionCommandTest {

  private static final Charset UTF_8 = Charset.forName("UTF-8");

  @Mock
  private RefineClient refineClient;

  private GetVersionCommand command;

  @BeforeEach
  public void setUp() throws MalformedURLException {
    refineClient = mock(RefineClient.class);
    when(refineClient.createUrl(anyString())).thenReturn(new URL("http://localhost:3333/"));
    command = RefineCommands.getVersion().build();
  }

  @Test
  public void should_execute() throws IOException {
    command.execute(refineClient);
    verify(refineClient).createUrl(anyString());
    verify(refineClient).execute(any(), any());
  }

  @Test
  public void should_parse_get_version_success_response() throws IOException, URISyntaxException {
    String responseBody =
        IOUtils.toString(getClass().getResource("/responseBody/get-version.json").toURI(), UTF_8);

    GetVersionResponse response = command.parseGetVersionResponse(responseBody);
    assertNotNull(response);
    assertEquals("OpenRefine 3.0-beta [TRUNK]", response.getFullName());
    assertEquals("3.0-beta [TRUNK]", response.getFullVersion());
    assertEquals("3.0-beta", response.getVersion());
    assertEquals("TRUNK", response.getRevision());
  }

  @Test
  public void should_throw_exception_when_not_parsable_as_get_version_response()
      throws IOException, URISyntaxException {
    String responseBody =
        IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);

    assertThrows(RefineException.class, () -> command.parseGetVersionResponse(responseBody));
  }

  @Test
  public void should_throw_exception_when_response_status_is_500() throws IOException {
    HttpResponse httpResponse = mockHttpResponse(500);

    assertThrows(RefineException.class, () -> command.handleResponse(httpResponse));
  }

  @Test
  public void should_throw_exception_when_response_body_is_no_json()
      throws IOException, URISyntaxException {
    String responseBody =
        IOUtils.toString(getClass().getResource("/responseBody/plain.txt").toURI(), UTF_8);
    HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

    assertThrows(RefineException.class, () -> command.handleResponse(httpResponse));
  }

  @Test
  public void should_throw_exception_when_not_parsable() throws IOException, URISyntaxException {
    String responseBody =
        IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);
    HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

    assertThrows(RefineException.class, () -> command.handleResponse(httpResponse));
  }

  @Test
  public void should_return_success_when_response_is_positive()
      throws IOException, URISyntaxException {
    String responseBody =
        IOUtils.toString(getClass().getResource("/responseBody/get-version.json").toURI(), UTF_8);
    HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

    GetVersionResponse response = command.handleResponse(httpResponse);
    assertNotNull(response);
    assertEquals("OpenRefine 3.0-beta [TRUNK]", response.getFullName());
    assertEquals("3.0-beta [TRUNK]", response.getFullVersion());
    assertEquals("3.0-beta", response.getVersion());
    assertEquals("TRUNK", response.getRevision());
  }
}
