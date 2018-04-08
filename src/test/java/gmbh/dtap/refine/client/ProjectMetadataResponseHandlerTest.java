package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.ImportOptionMetadata;
import gmbh.dtap.refine.api.RefineProject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
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
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;

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
public class ProjectMetadataResponseHandlerTest {

   private ProjectMetadataResponseHandler responseHandler;
   @Mock private HttpResponse httpResponse;
   @Mock private HttpEntity httpEntity;
   @Mock private StatusLine statusLine;

   @Before
   public void setUp() throws MalformedURLException {
      URL baseUrl = new URL("http://localhost:3333/");
      responseHandler = new ProjectMetadataResponseHandler(new ResponseParser(baseUrl));
   }

   @Test
   public void should_throw_exception_on_unexpected_status() throws IOException {
      when(statusLine.getStatusCode()).thenReturn(500);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);

      try {
         responseHandler.handleResponse(httpResponse);
         fail("expected exception not thrown");
      } catch (ClientProtocolException expectedException) {
         assertThat(expectedException.getMessage()).isEqualTo("Unexpected response status: 500");
      }
   }

   @Test
   public void should_throw_exception_on_malformed_json() throws IOException, JSONException {
      String expectedResponseBody = "This is clearly no JSON.";

      when(statusLine.getStatusCode()).thenReturn(200);
      when(httpEntity.getContent()).thenReturn(toInputStream(expectedResponseBody, "UTF-8"));
      when(httpResponse.getStatusLine()).thenReturn(statusLine);
      when(httpResponse.getEntity()).thenReturn(httpEntity);
      try {
         responseHandler.handleResponse(httpResponse);
         fail("expected exception not thrown");
      } catch (ClientProtocolException expectedException) {
         assertThat(expectedException.getMessage()).startsWith("Parser error:");
      }
   }

   @Test
   public void should_return_successful_but_empty_response() throws IOException, JSONException {
      String responseBody = "{}";

      when(statusLine.getStatusCode()).thenReturn(200);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);
      when(httpResponse.getEntity()).thenReturn(httpEntity);
      when(httpEntity.getContent()).thenReturn(toInputStream(responseBody, "UTF-8"));

      ProjectMetadataResponse projectMetadataResponse = responseHandler.handleResponse(httpResponse);
      assertThat(projectMetadataResponse).isNotNull();
      assertThat(projectMetadataResponse.getRefineProjects()).hasSize(0);
   }

   @Test
   public void should_return_successful_response() throws IOException, JSONException {
      String responseBody = IOUtils.toString(this.getClass().getResource("/testProjectMetadataResponseBody.json"), "UTF-8");

      when(statusLine.getStatusCode()).thenReturn(200);
      when(httpResponse.getStatusLine()).thenReturn(statusLine);
      when(httpResponse.getEntity()).thenReturn(httpEntity);
      when(httpEntity.getContent()).thenReturn(toInputStream(responseBody, "UTF-8"));

      ProjectMetadataResponse projectMetadataResponse = responseHandler.handleResponse(httpResponse);
      assertThat(projectMetadataResponse).isNotNull();
      assertThat(projectMetadataResponse.getRefineProjects()).hasSize(2);
      List<RefineProject> refineProjects = projectMetadataResponse.getRefineProjects();
      // Project 1
      RefineProject project1 = refineProjects.get(0);
      assertThat(project1.getId()).isEqualTo("2329924354543");
      assertThat(project1.getName()).isEqualTo("project name 1");
      assertThat(project1.getCreated()).isEqualTo(OffsetDateTime.parse("2018-04-07T12:05:00Z"));
      assertThat(project1.getModified()).isEqualTo(OffsetDateTime.parse("2018-04-07T12:05:53Z"));
      assertThat(project1.getCreator()).isEqualTo("creator 1");
      assertThat(project1.getContributors()).isEqualTo("contributors 1");
      assertThat(project1.getSubject()).isEqualTo("subject 1");
      assertThat(project1.getDescription()).isEqualTo("description 1");
      assertThat(project1.getRowCount()).isEqualTo(101);
      assertThat(project1.getCustomMetadata()).isNotNull(); // TODO: not implemented yet
      List<ImportOptionMetadata> importOptionMetadataList1 = project1.getImportOptionMetadata();
      assertThat(importOptionMetadataList1).hasSize(1);
      ImportOptionMetadata importOptionMetadata = importOptionMetadataList1.get(0);
      assertThat(importOptionMetadata.isStoreBlankRows()).isTrue();
      assertThat(importOptionMetadata.isIncludeFileSources()).isFalse();
      assertThat(importOptionMetadata.getSkipDataLines()).isEqualTo(0);
      assertThat(importOptionMetadata.isGuessCellValueTypes()).isFalse();
      assertThat(importOptionMetadata.getHeaderLines()).isEqualTo(1);
      assertThat(importOptionMetadata.getIgnoreLines()).isEqualTo(-1);
      assertThat(importOptionMetadata.isProcessQuotes()).isTrue();
      assertThat(importOptionMetadata.getFileSource()).isEqualTo("file1.csv");
      assertThat(importOptionMetadata.getProjectName()).isEqualTo("meta project name 1");
      assertThat(importOptionMetadata.getSeparator()).isEqualTo(",");
      assertThat(importOptionMetadata.isStoreBlankCellsAsNulls()).isTrue();
      // Project 2
      RefineProject project2 = refineProjects.get(1);
      assertThat(project2.getId()).isEqualTo("1659896150788");
      assertThat(project2.getName()).isEqualTo("project name 2");
      assertThat(project2.getCreated()).isEqualTo(OffsetDateTime.parse("2018-04-07T12:05:05Z"));
      assertThat(project2.getModified()).isEqualTo(OffsetDateTime.parse("2018-04-07T12:05:53Z"));
      assertThat(project2.getCreator()).isEqualTo("creator 2");
      assertThat(project2.getContributors()).isEqualTo("contributors 2");
      assertThat(project2.getSubject()).isEqualTo("subject 2");
      assertThat(project2.getDescription()).isEqualTo("description 2");
      assertThat(project2.getRowCount()).isEqualTo(102);
      assertThat(project2.getCustomMetadata()).isNotNull(); // TODO: not implemented yet
      assertThat(project2.getImportOptionMetadata()).hasSize(0);
   }
}
