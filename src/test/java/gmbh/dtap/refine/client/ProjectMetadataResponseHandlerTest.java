package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.ImportOptionMetadata;
import gmbh.dtap.refine.api.RefineException;
import gmbh.dtap.refine.api.RefineProject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.time.OffsetDateTime;
import java.util.List;

import static gmbh.dtap.refine.test.HttpMock.mockHttpResponse;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Tests for {@link DeleteProjectResponseHandler}.
 *
 * @since 0.1.2
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectMetadataResponseHandlerTest {

   private static final URI BASE_URL = URI.create("http://localhost:3333/");
   private static final Charset UTF_8 = Charset.forName("UTF-8");

   @Rule public ExpectedException thrown = ExpectedException.none();
   private ProjectMetadataResponseHandler projectMetadataResponseHandler;

   @Before
   public void setUp() throws MalformedURLException {
      projectMetadataResponseHandler = new ProjectMetadataResponseHandler(new ResponseParser(BASE_URL.toURL()));
   }

   @Test
   public void should_throw_exception_when_response_status_is_500() throws IOException {
      HttpResponse httpResponse = mockHttpResponse(500);

      thrown.expect(RefineException.class);
      projectMetadataResponseHandler.handleResponse(httpResponse);
   }

   @Test
   public void should_throw_exception_when_response_body_is_no_json() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/no-json.txt").toURI(), UTF_8);
      HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

      thrown.expect(RefineException.class);
      projectMetadataResponseHandler.handleResponse(httpResponse);
   }

   @Test
   public void should_return_successful_but_empty_response() throws IOException, JSONException {
      HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, "{}");

      ProjectMetadataResponse projectMetadataResponse = projectMetadataResponseHandler.handleResponse(httpResponse);
      assertThat(projectMetadataResponse).isNotNull();
      assertThat(projectMetadataResponse.getRefineProjects()).hasSize(0);
   }

   @Test
   public void should_return_successful_response() throws IOException, JSONException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/project-metadata.json").toURI(), UTF_8);
      HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

      ProjectMetadataResponse projectMetadataResponse = projectMetadataResponseHandler.handleResponse(httpResponse);
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
