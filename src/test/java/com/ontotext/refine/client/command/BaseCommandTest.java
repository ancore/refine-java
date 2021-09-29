package com.ontotext.refine.client.command;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.ontotext.refine.client.RefineClient;
import java.io.InputStream;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Provides base common logic that can be used in the concrete command tests.
 *
 * @author Antoniy Kunchev
 */
public abstract class BaseCommandTest<T, R extends RefineCommand<T>> {

  protected static final String PROJECT_ID = "1234567890987";
  protected static final String BASE_URI = "http://localhost:1937/";

  @Mock
  protected RefineClient client;

  /**
   * Provides the concrete command instance to be used in the tests.
   *
   * @return command instance
   */
  protected abstract R command();

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    when(client.createUri(anyString()))
        .thenAnswer(answer -> new URI(BASE_URI + answer.getArgument(0, String.class)));
  }

  /**
   * Provides the base test directory, where the resources for the test are. By default this method
   * returns 'responseBody/' as value.
   *
   * @return path to the directory with test resources
   */
  protected String getTestDir() {
    return "responseBody/";
  }

  /**
   * Loads test resource using the {@link #getTestDir()} method to retrieve the directory, where the
   * resource should be.
   *
   * @param resource to be loaded
   * @return {@link InputStream} of the loaded resource
   */
  protected InputStream loadResource(String resource) {
    return getClass().getClassLoader().getResourceAsStream(getTestDir() + resource);
  }

  /**
   * Creates response with status 'OK'.
   *
   * @param body for the response
   * @return HTTP response instance
   */
  protected HttpResponse okResponse(InputStream body) {
    BasicStatusLine statusLine = new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "OK");
    BasicHttpEntity entity = new BasicHttpEntity();
    entity.setContent(body);
    BasicHttpResponse response = new BasicHttpResponse(statusLine);
    response.setEntity(entity);
    return response;
  }
}
