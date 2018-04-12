package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.RefineException;
import gmbh.dtap.refine.api.RefineProjectLocation;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.MalformedURLException;

import static gmbh.dtap.refine.test.HttpMock.mockHttpResponse;
import static org.apache.http.HttpHeaders.LOCATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit Tests for {@link LocationResponseHandler}.
 *
 * @since 0.1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class LocationResponseHandlerTest {

   @Rule public ExpectedException thrown = ExpectedException.none();
   private LocationResponseHandler locationResponseHandler;

   @Before
   public void setUp() throws MalformedURLException {
      locationResponseHandler = new LocationResponseHandler();
   }


   @Test
   public void should_throw_exception_when_response_status_is_200() throws IOException {
      HttpResponse httpResponse = mockHttpResponse(200);

      thrown.expect(RefineException.class);
      locationResponseHandler.handleResponse(httpResponse);
   }

   @Test
   public void should_throw_exception_when_response_status_is_500() throws IOException {
      HttpResponse httpResponse = mockHttpResponse(500);

      thrown.expect(RefineException.class);
      locationResponseHandler.handleResponse(httpResponse);
   }

   @Test
   public void should_throw_exception_when_location_header_is_missing() throws IOException {
      HttpResponse httpResponse = mockHttpResponse(302);
      when(httpResponse.getHeaders(LOCATION)).thenReturn(null);
      when(httpResponse.getFirstHeader(LOCATION)).thenReturn(null);
      when(httpResponse.getLastHeader(LOCATION)).thenReturn(null);
      when(httpResponse.getAllHeaders()).thenReturn(null);

      thrown.expect(RefineException.class);
      locationResponseHandler.handleResponse(httpResponse);
   }

   @Test
   public void should_return_location_when_location_header_is_available() throws IOException, JSONException {
      String projectId = "617613761";
      String locationHeaderValue = "http://localhost:8080/project?project=" + projectId;
      RefineProjectLocation expectedLocation = MinimalRefineProjectLocation.from(locationHeaderValue);

      HttpResponse httpResponse = mockHttpResponse(302, new BasicHeader(LOCATION, locationHeaderValue));

      RefineProjectLocation actualLocation = locationResponseHandler.handleResponse(httpResponse);
      assertThat(actualLocation).isNotNull();
      assertThat(actualLocation.getId()).isEqualTo(expectedLocation.getId());
      assertThat(actualLocation.getUrl()).isEqualTo(expectedLocation.getUrl());
   }
}
