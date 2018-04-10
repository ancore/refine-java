package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.RefineException;
import gmbh.dtap.refine.api.RefineProjectLocation;
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
import java.util.Arrays;
import java.util.List;

import static gmbh.dtap.refine.client.MinimalRefineProjectLocation.from;
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

   private URL baseUrl;
   private MinimalRefineClient refineClient;
   @Mock private HttpClient mockHttpClient;

   @Before
   public void setUp() throws MalformedURLException {
      baseUrl = new URL("http://localhost:3333/");
      refineClient = new MinimalRefineClient(baseUrl, mockHttpClient);
   }

   @Test
   public void should_create_project() throws IOException {
      String projectId = "123456789";
      String projectName = "JUnit Test";
      File file = new File("names.csv");

      URL expectedUrl = new URL(baseUrl, "project?project=" + projectId);
      RefineProjectLocation expectedLocation = from(expectedUrl);

      LocationResponseHandler anyLocationResponseHandler = Mockito.any(LocationResponseHandler.class);
      when(mockHttpClient.execute(any(), anyLocationResponseHandler)).thenReturn(expectedLocation);

      RefineProjectLocation actualLocation = refineClient.createProject(projectName, file);
      assertThat(actualLocation).isEqualTo(expectedLocation);
      assertThat(actualLocation.getId()).isEqualTo(projectId);
      assertThat(actualLocation.getUrl()).isEqualTo(expectedUrl);
   }

   @Test
   public void should_delete_project() throws IOException {
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
      } catch (RefineException expectedException) {
         assertThat(expectedException.getMessage()).isEqualTo(expectedErrorMessage);
      }
   }

   @Test
   public void should_apply_operations() throws IOException {
      String projectId = "123456789";
      String operationJson = "{\n" +
            "    \"op\": \"core/column-split\",\n" +
            "    \"description\": \"Split column ID by separator\",\n" +
            "    \"engineConfig\": {\n" +
            "      \"facets\": [],\n" +
            "      \"mode\": \"row-based\"\n" +
            "    },\n" +
            "    \"columnName\": \"ID\",\n" +
            "    \"guessCellType\": true,\n" +
            "    \"removeOriginalColumn\": true,\n" +
            "    \"mode\": \"separator\",\n" +
            "    \"separator\": \"-\",\n" +
            "    \"regex\": false,\n" +
            "    \"maxColumns\": 0\n" +
            "  }";

      ApplyOperationsResponse applyOperationsResponse = ApplyOperationsResponse.ok();

      ApplyOperationsResponseHandler anyApplyOperationsResponseHandler = Mockito.any(ApplyOperationsResponseHandler.class);
      when(mockHttpClient.execute(any(), anyApplyOperationsResponseHandler)).thenReturn(applyOperationsResponse);

      refineClient.applyOperations(projectId, JsonOperation.from(operationJson));
   }

   @Test
   public void should_throw_exception_on_apply_operation() throws IOException {
      String projectId = "123456789";
      String operationJson = "{\n" +
            "    \"op\": \"core/column-split\",\n" +
            "    \"description\": \"Split column ID by separator\",\n" +
            "    \"engineConfig\": {\n" +
            "      \"facets\": [],\n" +
            "      \"mode\": \"row-based\"\n" +
            "    },\n" +
            "    \"columnName\": \"ID\",\n" +
            "    \"guessCellType\": true,\n" +
            "    \"removeOriginalColumn\": true,\n" +
            "    \"mode\": \"separator\",\n" +
            "    \"separator\": \"-\",\n" +
            "    \"regex\": false,\n" +
            "    \"maxColumns\": 0\n" +
            "  }";
      String expectedErrorMessage = "Error message.";

      ApplyOperationsResponse applyOperationsResponse = ApplyOperationsResponse.error(expectedErrorMessage);

      ApplyOperationsResponseHandler anyApplyOperationsResponseHandler = Mockito.any(ApplyOperationsResponseHandler.class);
      when(mockHttpClient.execute(any(), anyApplyOperationsResponseHandler)).thenReturn(applyOperationsResponse);

      try {
         refineClient.applyOperations(projectId, JsonOperation.from(operationJson));
         fail("expected exception not thrown");
      } catch (RefineException expectedException) {
         assertThat(expectedException.getMessage()).isEqualTo(expectedErrorMessage);
      }
   }

   @Test
   public void should_expression_preview() throws IOException {
      String projectId = "123456789";
      List<String> expectedExpressionPreviews = Arrays.asList("monaco", "denmark");

      ExpressionPreviewResponse expressionPreviewResponse = ExpressionPreviewResponse.ok(expectedExpressionPreviews);

      ExpressionPreviewResponseHandler anyExpressionPreviewResponseHandler = Mockito.any(ExpressionPreviewResponseHandler.class);
      when(mockHttpClient.execute(any(), anyExpressionPreviewResponseHandler)).thenReturn(expressionPreviewResponse);

      List<String> actualExpressionPreview = refineClient.expressionPreview(projectId, 4, new long[]{0, 1}, "grel:toLowercase(value);", false, 0);
      assertThat(actualExpressionPreview).isEqualTo(expectedExpressionPreviews);
   }

   @Test
   public void should_throw_exception_on_expression_preview() throws IOException {
      String projectId = "123456789";
      String expectedErrorMessage = "Server error";

      ExpressionPreviewResponse expressionPreviewResponse = ExpressionPreviewResponse.error(expectedErrorMessage);

      ExpressionPreviewResponseHandler anyExpressionPreviewResponseHandler = Mockito.any(ExpressionPreviewResponseHandler.class);
      when(mockHttpClient.execute(any(), anyExpressionPreviewResponseHandler)).thenReturn(expressionPreviewResponse);

      try {
         refineClient.expressionPreview(projectId, 4, new long[]{0, 1}, "grel:toLowercase(value);", false, 0);
         fail("expected exception not thrown");
      } catch (RefineException expectedException) {
         assertThat(expectedException.getMessage()).isEqualTo(expectedErrorMessage);
      }
   }
}
