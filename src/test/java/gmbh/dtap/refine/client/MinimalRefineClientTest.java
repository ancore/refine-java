package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.RefineClient;
import gmbh.dtap.refine.api.RefineProject;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit Tests for {@link MinimalRefineClient}.
 *
 * @since 0.1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class MinimalRefineClientTest {

   private URL url;
   private RefineClient refineClient;
   @Mock private HttpClient mockHttpClient;

   @Before
   public void setUp() throws MalformedURLException {
      url = new URL("http://localhost:3333/");
      refineClient = new MinimalRefineClient(url, mockHttpClient);
   }

   @Test
   public void should_create_project() throws IOException {
      String projectId = "123456789";
      String projectName = "JUnit Test";
      File file = new File("names.csv");
      URL expectedLocation = new URL(url, "project?project=" + projectId);

      LocationResponseHandler anyLocationResponseHandler = Mockito.any(LocationResponseHandler.class);
      when(mockHttpClient.execute(any(), anyLocationResponseHandler)).thenReturn(expectedLocation.toExternalForm());

      RefineProject project = refineClient.createProject(projectName, file);
      assertThat(project).isNotNull();
      assertThat(project.getId()).isNotEmpty();
      assertThat(project.getName()).isEqualTo(projectName);
      assertThat(project.getUrl()).isNotNull();
      assertThat(project.getUrl()).isEqualTo(expectedLocation);
   }

   @Test
   public void should_not_throw_exception_on_delete_project() throws IOException {
      String projectId = "123456789";

      DeleteProjectResponse deleteProjectResponse = DeleteProjectResponse.ok();

      DeleteProjectResponseHandler anyDeleteProjectResponseHandler = Mockito.any(DeleteProjectResponseHandler.class);
      when(mockHttpClient.execute(any(), anyDeleteProjectResponseHandler)).thenReturn(deleteProjectResponse);

      refineClient.deleteProject(projectId);
   }

   @Test
   public void should_throw_exception_on_delete_project() throws IOException {
      String projectId = "123456789";
      String expectedErrorMessage = "Error message.";

      DeleteProjectResponse deleteProjectResponse = DeleteProjectResponse.error(expectedErrorMessage);

      DeleteProjectResponseHandler anyDeleteProjectResponseHandler = Mockito.any(DeleteProjectResponseHandler.class);
      when(mockHttpClient.execute(any(), anyDeleteProjectResponseHandler)).thenReturn(deleteProjectResponse);

      try {
         refineClient.deleteProject(projectId);
         fail("expected exception not thrown");
      } catch (ClientProtocolException expectedException) {
         assertThat(expectedException.getMessage()).isEqualTo(expectedErrorMessage);
      }
   }
}
