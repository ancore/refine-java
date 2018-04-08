package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.RefineException;
import gmbh.dtap.refine.api.RefineProjectLocation;
import org.apache.http.Header;
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
   @Mock private HttpResponse httpResponse;
   @Mock private StatusLine statusLine;
   @Mock private Header header;

   @Before
   public void setUp() throws MalformedURLException {
      locationResponseHandler = new LocationResponseHandler();
   }

   @Test
   public void should_return_location() throws IOException, JSONException {
      String projectId = "617613761";
      String locationHeaderValue = "http://localhost:8080/project?project=" + projectId;
      RefineProjectLocation expectedLocation = MinimalRefineProjectLocation.from(locationHeaderValue);

      when(statusLine.getStatusCode()).thenReturn(302);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);
      when(header.getValue()).thenReturn(locationHeaderValue);
      when(httpResponse.getFirstHeader("Location")).thenReturn(header);

      RefineProjectLocation actualLocation = locationResponseHandler.handleResponse(httpResponse);
      assertThat(actualLocation).isNotNull();
      assertThat(actualLocation.getId()).isEqualTo(expectedLocation.getId());
      assertThat(actualLocation.getUrl()).isEqualTo(expectedLocation.getUrl());
   }

   @Test
   public void should_throw_exception_on_unexpected_status() throws IOException {
      when(statusLine.getStatusCode()).thenReturn(200);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);

      try {
         locationResponseHandler.handleResponse(httpResponse);
         fail("expected exception not thrown");
      } catch (RefineException expectedException) {
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
      } catch (RefineException expectedException) {
         assertThat(expectedException.getMessage()).isEqualTo("No location header found.");
      }
   }
}
