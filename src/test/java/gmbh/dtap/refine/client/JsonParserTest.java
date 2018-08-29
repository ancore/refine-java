package gmbh.dtap.refine.client;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static gmbh.dtap.refine.client.JsonParser.JSON_PARSER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Tests for {@link JsonParser}.
 */
public class JsonParserTest {

   private static final Charset UTF_8 = Charset.forName("UTF-8");

   @Rule
   public ExpectedException thrown = ExpectedException.none();

   @Test
   public void should_have_instance() {
      assertThat(JSON_PARSER).isNotNull();
   }

   @Test
   public void should_have_static_instance() {
      assertThat(JSON_PARSER).isEqualTo(JSON_PARSER);
   }

   @Test
   public void should_throw_exception_when_not_parsable_as_json() throws IOException, URISyntaxException {
      String plainText = IOUtils.toString(getClass().getResource("/responseBody/plain.txt").toURI(), UTF_8);

      thrown.expect(RefineException.class);
      JSON_PARSER.parseJson(plainText);
   }

   @Test
   public void should_parse_apply_operation_success_response() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/code-ok.json").toURI(), UTF_8);

      ApplyOperationsResponse response = JSON_PARSER.parseApplyOperationsResponse(responseBody);
      assertThat(response).isNotNull();
      assertThat(response.getCode()).isEqualTo(ResponseCode.OK);
      assertThat(response.getMessage()).isNull();
   }

   @Test
   public void should_parse_apply_operation_error_response() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);

      ApplyOperationsResponse response = JSON_PARSER.parseApplyOperationsResponse(responseBody);
      assertThat(response).isNotNull();
      assertThat(response.getCode()).isEqualTo(ResponseCode.ERROR);
      assertThat(response.getMessage()).isEqualTo("This is the error message.");
   }

   @Test
   public void should_parse_delete_project_success_response() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/code-ok.json").toURI(), UTF_8);

      DeleteProjectResponse response = JSON_PARSER.parseDeleteProjectResponse(responseBody);
      assertThat(response).isNotNull();
      assertThat(response.getCode()).isEqualTo(ResponseCode.OK);
      assertThat(response.getMessage()).isNull();
   }

   @Test
   public void should_parse_delete_project_error_response() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);

      DeleteProjectResponse response = JSON_PARSER.parseDeleteProjectResponse(responseBody);
      assertThat(response).isNotNull();
      assertThat(response.getCode()).isEqualTo(ResponseCode.ERROR);
      assertThat(response.getMessage()).isEqualTo("This is the error message.");
   }

   @Test
   public void should_parse_get_version_success_response() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/get-version.json").toURI(), UTF_8);

      GetVersionResponse response = JSON_PARSER.parseGetVersionResponse(responseBody);
      assertThat(response).isNotNull();
      assertThat(response.getFullName()).isEqualTo("OpenRefine 3.0-beta [TRUNK]");
      assertThat(response.getFullVersion()).isEqualTo("3.0-beta [TRUNK]");
      assertThat(response.getVersion()).isEqualTo("3.0-beta");
      assertThat(response.getRevision()).isEqualTo("TRUNK");
   }

   @Test
   public void should_throw_exception_when_not_parsable_as_get_version_response() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);

      thrown.expect(RefineException.class);
      JSON_PARSER.parseGetVersionResponse(responseBody);
   }

   @Test
   public void should_parse_expression_preview_success_response() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/expression-preview.json").toURI(), UTF_8);

      ExpressionPreviewResponse response = JSON_PARSER.parseExpressionPreviewResponse(responseBody);
      assertThat(response).isNotNull();
      assertThat(response.getCode()).isEqualTo(ResponseCode.OK);
      assertThat(response.getMessage()).isNull();
      assertThat(response.getExpressionPreviews()).containsExactly("7", "5", "5", "9");
   }

   @Test
   public void should_parse_expression_preview_error_response() throws IOException, URISyntaxException {
      String responseBody = IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);

      ExpressionPreviewResponse response = JSON_PARSER.parseExpressionPreviewResponse(responseBody);
      assertThat(response).isNotNull();
      assertThat(response.getCode()).isEqualTo(ResponseCode.ERROR);
      assertThat(response.getMessage()).isEqualTo("This is the error message.");
   }
}
