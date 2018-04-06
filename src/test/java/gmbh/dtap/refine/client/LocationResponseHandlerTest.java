package gmbh.dtap.refine.client;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

/**
 * Unit Tests for {@link LocationResponseHandler}.
 *
 * @since 0.1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class LocationResponseHandlerTest {

   private LocationResponseHandler locationResponseHandler;

   @Mock
   private HttpResponse httpResponse;

   @Mock
   private StatusLine statusLine;

   @Mock
   private Header header;

   @Before
   public void setUp() throws MalformedURLException {
      locationResponseHandler = new LocationResponseHandler();
   }

   @Test
   public void should_return_location() throws IOException, JSONException {
      String expectedLocation = "http://localhost:8080/project?project=foo";

      when(statusLine.getStatusCode()).thenReturn(302);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);
      when(header.getValue()).thenReturn(expectedLocation);
      when(httpResponse.getFirstHeader("Location")).thenReturn(header);

      String actualLocation = locationResponseHandler.handleResponse(httpResponse);
      assertThat(actualLocation).isNotNull();
      assertThat(actualLocation).isEqualTo(expectedLocation);
   }

   @Test
   public void should_throw_exception_on_unexpected_status() throws IOException {
      when(statusLine.getStatusCode()).thenReturn(200);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);

      try {
         locationResponseHandler.handleResponse(httpResponse);
         fail("expected exception not thrown");
      } catch (ClientProtocolException expectedException) {
         assertThat(expectedException.getMessage()).isEqualTo("Unexpected response status: 200");
      }
   }

   @Test
   public void should_throw_exception_on_missing_header() throws IOException {
      when(statusLine.getStatusCode()).thenReturn(302);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);
      when(httpResponse.getFirstHeader("Location")).thenReturn(null);

      try {
         locationResponseHandler.handleResponse(httpResponse);
         fail("expected exception not thrown");
      } catch (ClientProtocolException expectedException) {
         assertThat(expectedException.getMessage()).isEqualTo("No location header found.");
      }
   }
}
