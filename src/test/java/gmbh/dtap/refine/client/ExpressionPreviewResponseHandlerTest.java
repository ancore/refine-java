package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.RefineException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.json.JSONException;
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
import java.util.Arrays;

import static gmbh.dtap.refine.test.HttpMock.mockHttpResponse;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Tests for {@link ExpressionPreviewResponseHandler}.
 *
 * @since 0.1.5
 */
@RunWith(MockitoJUnitRunner.class)
public class ExpressionPreviewResponseHandlerTest {

   private static final URI BASE_URL = URI.create("http://localhost:3333/");
   private static final Charset UTF_8 = Charset.forName("UTF-8");

   @Rule public ExpectedException thrown = ExpectedException.none();

   private ExpressionPreviewResponseHandler expressionPreviewResponseHandler;

   @Before
   public void setUp() throws MalformedURLException {
      expressionPreviewResponseHandler = new ExpressionPreviewResponseHandler(new ResponseParser(BASE_URL.toURL()));
   }


   @Test
   public void should_throw_exception_when_response_status_is_500() throws IOException {
      HttpResponse httpResponse = mockHttpResponse(500);

      thrown.expect(RefineException.class);
      expressionPreviewResponseHandler.handleResponse(httpResponse);
   }

   @Test
   public void should_throw_exception_when_response_body_is_no_json() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/no-json.txt").toURI(), UTF_8);
      HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

      thrown.expect(RefineException.class);
      expressionPreviewResponseHandler.handleResponse(httpResponse);
   }

   @Test
   public void should_return_error_when_response_is_negative() throws IOException, URISyntaxException {
      String expectedMessage = "This is the error message.";
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);
      HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

      ExpressionPreviewResponse expressionPreviewResponse = expressionPreviewResponseHandler.handleResponse(httpResponse);
      assertThat(expressionPreviewResponse).isNotNull();
      assertThat(expressionPreviewResponse.isSuccessful()).isFalse();
      assertThat(expressionPreviewResponse.getMessage()).isEqualTo(expectedMessage);
   }

   @Test
   public void should_return_successful_response() throws IOException, JSONException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/expression-preview.json").toURI(), UTF_8);
      HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

      ExpressionPreviewResponse expressionPreviewResponse = expressionPreviewResponseHandler.handleResponse(httpResponse);
      assertThat(expressionPreviewResponse).isNotNull();
      assertThat(expressionPreviewResponse.isSuccessful()).isTrue();
      assertThat(expressionPreviewResponse.getMessage()).isNull();
      assertThat(expressionPreviewResponse.getExpressionPreviews()).isEqualTo(Arrays.asList("7", "5", "5", "9"));
   }
}
