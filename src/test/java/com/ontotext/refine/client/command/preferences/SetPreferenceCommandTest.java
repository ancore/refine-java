package com.ontotext.refine.client.command.preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.ResponseCode;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test for {@link SetPreferenceCommand}.
 *
 * @author Antoniy Kunchev
 */
class SetPreferenceCommandTest {

  private static final String TEST_PROPERTY = "test.prop";

  private SetPreferenceCommand command;

  @Mock
  private RefineClient client;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    command = RefineCommands
        .setPreference()
        .setProperty(TEST_PROPERTY)
        .setValue("value")
        .setToken("test-token")
        .build();

    when(client.createUrl(anyString()))
        .then(answer -> new URL("http://localhost:3333" + answer.getArgument(0)));
  }

  @Test
  void execute_successful() throws IOException {
    HttpResponse httpRes =
        buildResponse(new ByteArrayInputStream("{\"code\" : \"ok\"}".getBytes()), HttpStatus.SC_OK);
    doAnswer(answer -> answer.getArgument(1, ResponseHandler.class).handleResponse(httpRes))
        .when(client)
        .execute(any(), any());

    SetPreferenceCommandResponse response = command.execute(client);
    assertNotNull(response);
    assertEquals(ResponseCode.OK, response.getCode());
  }

  @Test
  void execute_failed() throws IOException {
    HttpResponse httpRes = buildResponse(
        new ByteArrayInputStream("{\"code\" : \"error\", \"message\" : \"Test error\"}".getBytes()),
        HttpStatus.SC_OK);
    doAnswer(answer -> answer.getArgument(1, ResponseHandler.class).handleResponse(httpRes))
        .when(client)
        .execute(any(), any());

    SetPreferenceCommandResponse response = command.execute(client);
    assertNotNull(response);
    assertEquals(ResponseCode.ERROR, response.getCode());
    assertEquals("Test error", response.getMessage());
  }

  @Test
  void execute_failedDueExc() throws IOException {
    doThrow(new RefineException("Generic test error")).when(client).execute(any(), any());

    RefineException exc = assertThrows(RefineException.class, () -> command.execute(client));
    assertEquals("Generic test error", exc.getMessage());
  }

  @Test
  void execute_failedDueIoExc() throws IOException {
    doThrow(new IOException("IO test error")).when(client).execute(any(), any());

    RefineException exc = assertThrows(RefineException.class, () -> command.execute(client));
    assertEquals(
        "Failed to set preference for property: 'test.prop' due to: IO test error",
        exc.getMessage());
  }

  @Test
  void execute_failedResponseParsing() throws IOException {
    HttpResponse httpRes = buildResponse(
        new ByteArrayInputStream("{\"code\" : \"something-else\"}".getBytes()), HttpStatus.SC_OK);
    doAnswer(answer -> answer.getArgument(1, ResponseHandler.class).handleResponse(httpRes))
        .when(client)
        .execute(any(), any());

    RefineException exc = assertThrows(RefineException.class, () -> command.execute(client));
    assertEquals(
        "Failed to parse the response from the set preference operation for property: test.prop",
        exc.getMessage());
  }

  private HttpResponse buildResponse(InputStream is, int status) {
    HttpResponse response =
        new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, status, ""));
    BasicHttpEntity entity = new BasicHttpEntity();
    entity.setContent(is);
    response.setEntity(entity);
    return response;
  }
}
