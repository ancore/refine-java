package gmbh.dtap.refine.test;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;

import java.io.IOException;

import static org.apache.commons.io.IOUtils.toInputStream;
import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Provides common http mocks for the Refine-Java unit tests.
 */
public class HttpMock {

   /**
    * Prevents instantiation.
    */
   private HttpMock() {
   }

   /**
    * Returns a mock with the given HTTP status code.
    *
    * @param statusCode the HTTP status code
    * @return the mocked response
    */
   public static HttpResponse mockHttpResponse(int statusCode) {
      StatusLine statusLine = mock(StatusLine.class);
      when(statusLine.getStatusCode()).thenReturn(statusCode);
      when(statusLine.getReasonPhrase()).thenReturn("Mock");

      HttpResponse httpResponse = mock(HttpResponse.class);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);

      return httpResponse;
   }

   /**
    * Returns a mock with the given HTTP status code and headers.
    *
    * @param statusCode the HTTP status code
    * @param headers    the headers
    * @return the mocked response
    */
   public static HttpResponse mockHttpResponse(int statusCode, Header... headers) {
      notNull(headers, "headers");

      StatusLine statusLine = mock(StatusLine.class);
      when(statusLine.getStatusCode()).thenReturn(statusCode);
      when(statusLine.getReasonPhrase()).thenReturn("Mock");

      HttpResponse httpResponse = mock(HttpResponse.class);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);

      // skipping HeaderIterator
      for (Header header : headers) {
         when(httpResponse.getFirstHeader(header.getName())).thenReturn(header);
         when(httpResponse.getLastHeader(header.getName())).thenReturn(header);
         when(httpResponse.getHeaders(header.getName())).thenReturn(new Header[]{header});
      }
      when(httpResponse.getAllHeaders()).thenReturn(headers);

      return httpResponse;
   }

   /**
    * Returns a mock with the given HTTP status code, content type and response body content.
    *
    * @param statusCode   the HTTP status code
    * @param contentType  the content type
    * @param responseBody the response body content
    * @return the mocked response
    * @throws IOException in case the response body conversion to stream fails
    */
   public static HttpResponse mockHttpResponse(int statusCode, ContentType contentType, String responseBody) throws IOException {
      notNull(contentType, "contentType");
      notNull(responseBody, "responseBody");

      StatusLine statusLine = mock(StatusLine.class);
      when(statusLine.getStatusCode()).thenReturn(statusCode);
      when(statusLine.getReasonPhrase()).thenReturn("Mock");

      HttpEntity httpEntity = mock(HttpEntity.class);
      when(httpEntity.getContent()).thenReturn(toInputStream(responseBody, contentType.getCharset()));

      Header[] headers = new Header[]{
            new BasicHeader(CONTENT_TYPE, contentType.toString())
      };

      HttpResponse httpResponse = mock(HttpResponse.class);
      when(httpResponse.getHeaders(CONTENT_TYPE)).thenReturn(headers);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);
      when(httpResponse.getEntity()).thenReturn(httpEntity);

      return httpResponse;
   }
}
