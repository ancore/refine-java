package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.RefineException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static org.apache.commons.io.IOUtils.toInputStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

/**
 * Unit Tests for {@link ExpressionPreviewResponseHandler}.
 *
 * @since 0.1.5
 */
@RunWith(MockitoJUnitRunner.class)
public class ExpressionPreviewResponseHandlerTest {

   private ExpressionPreviewResponseHandler responseHandler;
   @Mock private HttpResponse httpResponse;
   @Mock private HttpEntity httpEntity;
   @Mock private StatusLine statusLine;

   @Before
   public void setUp() throws MalformedURLException {
      URL baseUrl = new URL("http://localhost:3333/");
      responseHandler = new ExpressionPreviewResponseHandler(new ResponseParser(baseUrl));
   }

   @Test
   public void should_throw_exception_on_unexpected_status() throws IOException {
      when(statusLine.getStatusCode()).thenReturn(500);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);

      try {
         responseHandler.handleResponse(httpResponse);
         fail("expected exception not thrown");
      } catch (RefineException expectedException) {
         assertThat(expectedException.getMessage()).isEqualTo("Unexpected response status: 500");
      }
   }

   @Test
   public void should_throw_exception_on_malformed_json() throws IOException, JSONException {
      String expectedResponseBody = "That's a bingo!";

      when(statusLine.getStatusCode()).thenReturn(200);
      when(httpEntity.getContent()).thenReturn(toInputStream(expectedResponseBody, "UTF-8"));
      when(httpResponse.getStatusLine()).thenReturn(statusLine);
      when(httpResponse.getEntity()).thenReturn(httpEntity);
      
      try {
         responseHandler.handleResponse(httpResponse);
         fail("expected exception not thrown");
      } catch (RefineException expectedException) {
         assertThat(expectedException.getMessage()).startsWith("Parser error:");
      }
   }

   @Test
   public void should_return_successful_response() throws IOException, JSONException {
      String responseBody = "{\"code\":\"ok\",\"results\":[\"7\",\"5\",\"5\",\"9\"]}";

      when(statusLine.getStatusCode()).thenReturn(200);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);
      when(httpResponse.getEntity()).thenReturn(httpEntity);
      when(httpEntity.getContent()).thenReturn(toInputStream(responseBody, "UTF-8"));

      ExpressionPreviewResponse expressionPreviewResponse = responseHandler.handleResponse(httpResponse);
      assertThat(expressionPreviewResponse).isNotNull();
      assertThat(expressionPreviewResponse.isSuccessful()).isTrue();
      assertThat(expressionPreviewResponse.getMessage()).isNull();
      assertThat(expressionPreviewResponse.getExpressionPreviews()).isEqualTo(Arrays.asList("7", "5", "5", "9"));
   }

   @Test
   public void should_return_error_response_with_message() throws IOException, JSONException {
      String expectedMessage = "For input string: \"foo\"";
      String responseBody = "{\n" +
            "    \"code\": \"error\",\n" +
            "    \"message\": \"For input string: \\\"foo\\\"\"" +
            "}";

      when(statusLine.getStatusCode()).thenReturn(200);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);
      when(httpResponse.getEntity()).thenReturn(httpEntity);
      when(httpEntity.getContent()).thenReturn(toInputStream(responseBody, "UTF-8"));

      ExpressionPreviewResponse expressionPreviewResponse = responseHandler.handleResponse(httpResponse);
      assertThat(expressionPreviewResponse).isNotNull();
      assertThat(expressionPreviewResponse.isSuccessful()).isFalse();
      assertThat(expressionPreviewResponse.getMessage()).isEqualTo(expectedMessage);
   }
}
