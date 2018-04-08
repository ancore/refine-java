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

import static org.apache.commons.io.IOUtils.toInputStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

/**
 * Unit Tests for {@link DeleteProjectResponseHandler}.
 *
 * @since 0.1.2
 */
@RunWith(MockitoJUnitRunner.class)
public class DeleteProjectResponseHandlerTest {

   private DeleteProjectResponseHandler responseHandler;
   @Mock private HttpResponse httpResponse;
   @Mock private HttpEntity httpEntity;
   @Mock private StatusLine statusLine;

   @Before
   public void setUp() throws MalformedURLException {
      URL baseUrl = new URL("http://localhost:3333/");
      responseHandler = new DeleteProjectResponseHandler(new ResponseParser(baseUrl));
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
      String expectedResponseBody = "This is no valid JSON.";

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
      String responseBody = "{ \"code\" : \"ok\" }";

      when(statusLine.getStatusCode()).thenReturn(200);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);
      when(httpResponse.getEntity()).thenReturn(httpEntity);
      when(httpEntity.getContent()).thenReturn(toInputStream(responseBody, "UTF-8"));

      DeleteProjectResponse deleteProjectResponse = responseHandler.handleResponse(httpResponse);
      assertThat(deleteProjectResponse).isNotNull();
      assertThat(deleteProjectResponse.isSuccessful()).isTrue();
      assertThat(deleteProjectResponse.getMessage()).isNull();
   }

   @Test
   public void should_return_error_response_with_message() throws IOException, JSONException {
      String expectedMessage = "For input string: \"foo\"";
      String responseBody = "{\n" +
            "    \"code\": \"error\",\n" +
            "    \"message\": \"For input string: \\\"foo\\\"\",\n" +
            "    \"stack\": \"java.lang.NumberFormatException: For input string: \\\"ss\\\"\\n\\tat java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)\\n\\tat java.lang.Long.parseLong(Long.java:589)\\n\\tat java.lang.Long.parseLong(Long.java:631)\\n\\tat com.google.refine.commands.project.DeleteProjectCommand.doPost(DeleteProjectCommand.java:51)\\n\\tat com.google.refine.RefineServlet.service(RefineServlet.java:177)\\n\\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:820)\\n\\tat org.mortbay.jetty.servlet.ServletHolder.handle(ServletHolder.java:511)\\n\\tat org.mortbay.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1166)\\n\\tat org.mortbay.servlet.UserAgentFilter.doFilter(UserAgentFilter.java:81)\\n\\tat org.mortbay.servlet.GzipFilter.doFilter(GzipFilter.java:132)\\n\\tat org.mortbay.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1157)\\n\\tat org.mortbay.jetty.servlet.ServletHandler.handle(ServletHandler.java:388)\\n\\tat org.mortbay.jetty.security.SecurityHandler.handle(SecurityHandler.java:216)\\n\\tat org.mortbay.jetty.servlet.SessionHandler.handle(SessionHandler.java:182)\\n\\tat org.mortbay.jetty.handler.ContextHandler.handle(ContextHandler.java:765)\\n\\tat org.mortbay.jetty.webapp.WebAppContext.handle(WebAppContext.java:418)\\n\\tat org.mortbay.jetty.handler.HandlerWrapper.handle(HandlerWrapper.java:152)\\n\\tat org.mortbay.jetty.Server.handle(Server.java:326)\\n\\tat org.mortbay.jetty.HttpConnection.handleRequest(HttpConnection.java:542)\\n\\tat org.mortbay.jetty.HttpConnection$RequestHandler.content(HttpConnection.java:938)\\n\\tat org.mortbay.jetty.HttpParser.parseNext(HttpParser.java:755)\\n\\tat org.mortbay.jetty.HttpParser.parseAvailable(HttpParser.java:218)\\n\\tat org.mortbay.jetty.HttpConnection.handle(HttpConnection.java:404)\\n\\tat org.mortbay.jetty.bio.SocketConnector$Connection.run(SocketConnector.java:228)\\n\\tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\\n\\tat java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\\n\\tat java.lang.Thread.run(Thread.java:745)\\n\"\n" +
            "}";

      when(statusLine.getStatusCode()).thenReturn(200);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);
      when(httpResponse.getEntity()).thenReturn(httpEntity);
      when(httpEntity.getContent()).thenReturn(toInputStream(responseBody, "UTF-8"));

      DeleteProjectResponse deleteProjectResponse = responseHandler.handleResponse(httpResponse);
      assertThat(deleteProjectResponse).isNotNull();
      assertThat(deleteProjectResponse.isSuccessful()).isFalse();
      assertThat(deleteProjectResponse.getMessage()).isEqualTo(expectedMessage);
   }
}
