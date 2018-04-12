package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.RefineException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static gmbh.dtap.refine.test.HttpMock.mockHttpResponse;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Tests for {@link ApplyOperationsResponseHandler}.
 *
 * @since 0.1.4
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplyOperationsResponseHandlerTest {

   private static final URI BASE_URL = URI.create("http://localhost:3333/");
   private static final Charset UTF_8 = Charset.forName("UTF-8");

   @Rule public ExpectedException thrown = ExpectedException.none();
   private ApplyOperationsResponseHandler applyOperationsResponseHandler;

   @Before
   public void setUp() throws MalformedURLException {
      applyOperationsResponseHandler = new ApplyOperationsResponseHandler(new ResponseParser(BASE_URL.toURL()));
   }

   @Test
   public void should_throw_exception_when_response_status_is_500() throws IOException {
      HttpResponse httpResponse = mockHttpResponse(500);

      thrown.expect(RefineException.class);
      applyOperationsResponseHandler.handleResponse(httpResponse);
   }

   @Test
   public void should_throw_exception_when_response_body_is_no_json() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/no-json.txt").toURI(), UTF_8);
      HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

      thrown.expect(RefineException.class);
      applyOperationsResponseHandler.handleResponse(httpResponse);
   }

   @Test
   public void should_return_error_when_response_is_negative() throws IOException, URISyntaxException {
      String expectedMessage = "This is the error message.";
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);
      HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

      ApplyOperationsResponse applyOperationsResponse = applyOperationsResponseHandler.handleResponse(httpResponse);
      assertThat(applyOperationsResponse).isNotNull();
      assertThat(applyOperationsResponse.isSuccessful()).isFalse();
      assertThat(applyOperationsResponse.getMessage()).isEqualTo(expectedMessage);
   }

   @Test
   public void should_return_success_when_response_is_positive() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/code-ok.json").toURI(), UTF_8);
      HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

      ApplyOperationsResponse applyOperationsResponse = applyOperationsResponseHandler.handleResponse(httpResponse);
      assertThat(applyOperationsResponse).isNotNull();
      assertThat(applyOperationsResponse.isSuccessful()).isTrue();
      assertThat(applyOperationsResponse.getMessage()).isNull();
   }
}
