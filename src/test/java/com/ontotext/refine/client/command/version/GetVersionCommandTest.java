package com.ontotext.refine.client.command.version;

import static com.ontotext.refine.client.testsupport.HttpMock.mockHttpResponse;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Unit Tests for {@link GetVersionCommand}.
 */
class GetVersionCommandTest {

  private static final Charset UTF_8 = Charset.forName("UTF-8");

  @Mock
  private RefineClient refineClient;

  private GetVersionCommand command;

  @BeforeEach
  void setUp() throws MalformedURLException {
    MockitoAnnotations.openMocks(this);

    when(refineClient.createUrl(anyString())).thenReturn(new URL("http://localhost:3333/"));
    command = RefineCommands.getVersion().build();
  }

  @Test
  void should_execute() throws IOException {
    command.execute(refineClient);
    verify(refineClient).createUrl(anyString());
    verify(refineClient).execute(any(), any());
  }

  @Test
  void should_parse_get_version_success_response() throws IOException, URISyntaxException {
    String responseBody =
        IOUtils.toString(getClass().getResource("/responseBody/get-version.json").toURI(), UTF_8);

    GetVersionResponse response = command.handleResponse(buildHttpResponse(responseBody));
    assertNotNull(response);
    assertEquals("OpenRefine 3.0-beta [TRUNK]", response.getFullName());
    assertEquals("3.0-beta [TRUNK]", response.getFullVersion());
    assertEquals("3.0-beta", response.getVersion());
    assertEquals("TRUNK", response.getRevision());
  }

  @Test
  void should_throw_exception_when_not_parsable_as_get_version_response()
      throws IOException, URISyntaxException {
    String responseBody =
        IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);

    BasicHttpResponse httpResponse = buildHttpResponse(responseBody);
    assertThrows(RefineException.class, () -> command.handleResponse(httpResponse));
  }

  private BasicHttpResponse buildHttpResponse(String responseBody)
      throws UnsupportedEncodingException {
    BasicHttpResponse response =
        new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, ""));
    response.setEntity(new StringEntity(responseBody));
    return response;
  }

  @Test
  void should_throw_exception_when_response_status_is_500() throws IOException {
    HttpResponse httpResponse = mockHttpResponse(500);

    assertThrows(RefineException.class, () -> command.handleResponse(httpResponse));
  }

  @Test
  void should_throw_exception_when_response_body_is_no_json()
      throws IOException, URISyntaxException {
    String responseBody =
        IOUtils.toString(getClass().getResource("/responseBody/plain.txt").toURI(), UTF_8);
    HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

    assertThrows(RefineException.class, () -> command.handleResponse(httpResponse));
  }

  @Test
  void should_throw_exception_when_not_parsable() throws IOException, URISyntaxException {
    String responseBody =
        IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);
    HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

    assertThrows(RefineException.class, () -> command.handleResponse(httpResponse));
  }

  @Test
  void should_return_success_when_response_is_positive()
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
