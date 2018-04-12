package gmbh.dtap.refine.client;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static gmbh.dtap.refine.test.HttpMock.mockHttpResponse;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Tests for {@link StreamResponseHandler}.
 *
 * @since 0.1.1
 */
@RunWith(MockitoJUnitRunner.class)
public class StreamResponseHandlerTest {

   private static final URI BASE_URL = URI.create("http://localhost:3333/");
   private static final Charset UTF_8 = Charset.forName("UTF-8");

   @Rule public ExpectedException thrown = ExpectedException.none();
   private StreamResponseHandler streamResponseHandler;
   private ByteArrayOutputStream byteArrayOutputStream;

   @Before
   public void setUp() throws MalformedURLException {
      byteArrayOutputStream = new ByteArrayOutputStream();
      streamResponseHandler = new StreamResponseHandler(byteArrayOutputStream);
   }

   @Test
   public void should_stream_rows() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/export-rows.csv").toURI(), UTF_8);
      HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

      streamResponseHandler.handleResponse(httpResponse);
      assertThat(byteArrayOutputStream.toString(UTF_8.name())).isEqualTo(responseBody);
   }
}
