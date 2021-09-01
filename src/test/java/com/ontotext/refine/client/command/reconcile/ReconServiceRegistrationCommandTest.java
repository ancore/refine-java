package com.ontotext.refine.client.command.reconcile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
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
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;


/**
 * Test for {@link ReconServiceRegistrationCommand}.
 *
 * @author Antoniy Kunchev
 */
class ReconServiceRegistrationCommandTest {

  private static final String RECON_SERVICE_URI = "http://test-recon-service.com/reconciliation";
  private static final String OR_BASE_URI = "http://localhost:3333";
  private static final String OR_COMMAND_URI = OR_BASE_URI + "/orefine/command/core/";
  private static final String GET_PREFERENCE_URI =
      OR_COMMAND_URI + "get-preference?name=reconciliation.standardServices";
  private static final String SET_PREFERENCE_URI =
      OR_COMMAND_URI + "set-preference?name=reconciliation.standardServices";

  private ReconServiceRegistrationCommand command;

  @Mock
  private RefineClient client;

  @BeforeEach
  void setup() throws MalformedURLException {
    MockitoAnnotations.openMocks(this);

    command = RefineCommands
        .registerReconciliationService()
        .setService(RECON_SERVICE_URI)
        .setToken("test-token")
        .build();

    when(client.createUrl(anyString()))
        .then(answer -> new URL(OR_BASE_URI + answer.getArgument(0)));
  }

  @Test
  void execute_successful() throws IOException {
    HttpResponse getPrefRes = buildResponse(
        loadResource("/reconcile/get-preference-recon-services-response.json"), HttpStatus.SC_OK);
    doAnswer(buildRequestExecutionAnswer(getPrefRes))
        .when(client)
        .execute(matchUri(GET_PREFERENCE_URI), any());

    HttpResponse reconServiceRes = buildResponse(
        loadResource("/reconcile/test-recon-service-response.json"), HttpStatus.SC_OK);
    doAnswer(buildRequestExecutionAnswer(reconServiceRes))
        .when(client)
        .execute(matchUri(RECON_SERVICE_URI), any());

    HttpResponse setPrefRes =
        buildResponse(new ByteArrayInputStream("{\"code\" : \"ok\"}".getBytes()), HttpStatus.SC_OK);
    doAnswer(buildRequestExecutionAnswer(setPrefRes))
        .when(client)
        .execute(matchUri(SET_PREFERENCE_URI), any());

    ReconServiceRegistrationCommandResponse response = command.execute(client);
    assertNotNull(response);
    assertEquals(ResponseCode.OK, response.getCode());
  }

  private Answer<?> buildRequestExecutionAnswer(HttpResponse getPrefRes) {
    return answer -> answer.getArgument(1, ResponseHandler.class).handleResponse(getPrefRes);
  }

  private static InputStream loadResource(String file) {
    return ReconServiceRegistrationCommandTest.class.getResourceAsStream(file);
  }

  private HttpResponse buildResponse(InputStream is, int status) {
    HttpResponse response =
        new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, status, ""));
    BasicHttpEntity entity = new BasicHttpEntity();
    entity.setContent(is);
    response.setEntity(entity);
    return response;
  }

  private HttpUriRequest matchUri(String uri) {
    return argThat(req -> uri.equals(req.getURI().toString()));
  }

  @Test
  void execute_failPrefRetrieval() throws IOException {
    doThrow(new RefineException("Test error"))
        .when(client)
        .execute(matchUri(GET_PREFERENCE_URI), any());

    RefineException exc = assertThrows(RefineException.class, () -> command.execute(client));
    assertEquals("Failed to retrieve preferences due to: Test error", exc.getMessage());
  }

  @Test
  void execute_failPrefRetrievalDueIoError() throws IOException {
    HttpResponse getPrefRes = buildResponse(
        loadResource("/reconcile/get-preference-recon-services-response.json"), HttpStatus.SC_OK);
    doAnswer(buildRequestExecutionAnswer(getPrefRes))
        .when(client)
        .execute(matchUri(GET_PREFERENCE_URI), any());

    doThrow(new IOException("IO test error"))
        .when(client)
        .execute(matchUri(RECON_SERVICE_URI), any());

    RefineException exc = assertThrows(RefineException.class, () -> command.execute(client));
    String expected = String.format(
        "Failed to register reconciliation service: '%s' due to: IO test error", RECON_SERVICE_URI);
    assertEquals(expected, exc.getMessage());
  }
}
