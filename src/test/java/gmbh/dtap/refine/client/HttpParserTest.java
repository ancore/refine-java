package gmbh.dtap.refine.client;

import gmbh.dtap.refine.test.HttpMock;
import org.apache.http.HttpResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static gmbh.dtap.refine.client.HttpParser.HTTP_PARSER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Tests for {@link HttpParser}.
 */
public class HttpParserTest {

   @Rule
   public ExpectedException thrown = ExpectedException.none();

   @Test
   public void should_have_instance() {
      assertThat(HTTP_PARSER).isNotNull();
   }

   @Test
   public void should_have_static_instance() {
      assertThat(HTTP_PARSER).isEqualTo(HTTP_PARSER);
   }

   @Test
   public void should_throw_exception_when_status_code_not_assured() throws IOException {
      HttpResponse badRequestResponse = HttpMock.mockHttpResponse(500);

      thrown.expect(RefineException.class);
      HTTP_PARSER.assureStatusCode(badRequestResponse, 200);
   }

   @Test
   public void should_not_throw_exception_when_status_code_is_assured() throws IOException {
      HttpResponse okResponse = HttpMock.mockHttpResponse(200);

      HTTP_PARSER.assureStatusCode(okResponse, 200);
   }
}
