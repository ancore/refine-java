package gmbh.dtap.refine.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.apache.commons.io.IOUtils.toInputStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit Tests for {@link StreamResponseHandler}.
 *
 * @since 0.1.1
 */
@RunWith(MockitoJUnitRunner.class)
public class StreamResponseHandlerTest {

   private StreamResponseHandler streamResponseHandler;
   private ByteArrayOutputStream byteArrayOutputStream;
   @Mock private HttpResponse httpResponse;
   @Mock private HttpEntity httpEntity;
   @Mock private StatusLine statusLine;

   @Before
   public void setUp() throws MalformedURLException {
      byteArrayOutputStream = new ByteArrayOutputStream();
      streamResponseHandler = new StreamResponseHandler(byteArrayOutputStream);
   }

   @Test
   public void should_stream_rows() throws IOException {
      String expectedRows = "ID,Street,Zip,City,Country\n" +
            "ROW-1,7442 At Rd.,7638 BW,Cavallino,Mona×o\n" +
            "ROW-2,Ap #877-4799 Nibh. Rd.,63568,Virginia Beach,Denmark\n";

      when(statusLine.getStatusCode()).thenReturn(200);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);
      when(httpResponse.getEntity()).thenReturn(httpEntity);
      when(httpEntity.getContent()).thenReturn(toInputStream(expectedRows, "UTF-8"));

      streamResponseHandler.handleResponse(httpResponse);
      assertThat(byteArrayOutputStream.toString("UTF-8")).isEqualTo(expectedRows);
   }
}
