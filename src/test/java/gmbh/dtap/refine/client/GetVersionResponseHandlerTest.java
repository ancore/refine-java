package gmbh.dtap.refine.client;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static gmbh.dtap.refine.test.HttpMock.mockHttpResponse;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Tests for {@link GetVersionResponseHandler}.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetVersionResponseHandlerTest {

   private static final Charset UTF_8 = Charset.forName("UTF-8");

   @Rule
   public ExpectedException thrown = ExpectedException.none();
   private GetVersionResponseHandler getVersionResponseHandler;

   @Before
   public void setUp() {
      getVersionResponseHandler = new GetVersionResponseHandler();
   }

   @Test
   public void should_throw_exception_when_response_status_is_500() throws IOException {
      HttpResponse httpResponse = mockHttpResponse(500);

      thrown.expect(RefineException.class);
      getVersionResponseHandler.handleResponse(httpResponse);
   }

   @Test
   public void should_throw_exception_when_response_body_is_no_json() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/plain.txt").toURI(), UTF_8);
      HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

      thrown.expect(RefineException.class);
      getVersionResponseHandler.handleResponse(httpResponse);
   }

   @Test
   public void should_throw_exception_when_not_parsable() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);
      HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

      thrown.expect(RefineException.class);
      getVersionResponseHandler.handleResponse(httpResponse);
   }

   @Test
   public void should_return_success_when_response_is_positive() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/get-version.json").toURI(), UTF_8);
      HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

      GetVersionResponse response = getVersionResponseHandler.handleResponse(httpResponse);
      assertThat(response).isNotNull();
      assertThat(response.getFullName()).isEqualTo("OpenRefine 3.0-beta [TRUNK]");
      assertThat(response.getFullVersion()).isEqualTo("3.0-beta [TRUNK]");
      assertThat(response.getVersion()).isEqualTo("3.0-beta");
      assertThat(response.getRevision()).isEqualTo("TRUNK");
   }
}
