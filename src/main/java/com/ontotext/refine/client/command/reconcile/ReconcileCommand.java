package com.ontotext.refine.client.command.reconcile;

import static com.ontotext.refine.client.util.HttpParser.HTTP_PARSER;
import static com.ontotext.refine.client.util.JsonParser.JSON_PARSER;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommand;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;


/**
 * A command that performs reconciliation process over the project data.
 *
 * @author Antoniy Kunchev
 */
public class ReconcileCommand implements RefineCommand<ReconcileCommandResponse> {

  private static final String CONTENT_TYPE =
      ContentType.APPLICATION_FORM_URLENCODED.withCharset(StandardCharsets.UTF_8).toString();

  private static final JsonNode CONFIG;
  private static final JsonNode ENGINE;

  static {
    JsonNodeFactory factory = JsonNodeFactory.instance;

    // TODO find a way to extract the data that is required to build correct configuration
    CONFIG = factory.objectNode()
        .put("mode", "standard-service")
        .put("service", "https://wikidata.reconci.link/en/api")
        .put("identifierSpace", "http://www.wikidata.org/entity/")
        .put("schemaSpace", "http://www.wikidata.org/prop/direct/")
        .put("autoMatch", true)
        .put("limit", 0)
        .putRawValue("columnDetails", new RawValue(factory.arrayNode()));

    ENGINE = factory.objectNode()
        .put("mode", "row-based")
        .putRawValue("facets", new RawValue(factory.arrayNode()));
  }

  private final String project;
  private final String column;
  private final ColumnType columnType;
  private final String token;

  private ReconcileCommand(String project, String column, String token, ColumnType columnType) {
    this.project = project;
    this.column = column;
    this.columnType = columnType;
    this.token = token;
  }

  @Override
  public String endpoint() {
    return "/orefine/command/core/reconcile";
  }

  @Override
  public ReconcileCommandResponse execute(RefineClient client) throws RefineException {
    try {
      ObjectNode configuration = CONFIG.deepCopy();

      HttpUriRequest request = RequestBuilder
          .post(client.createUrl(endpoint()).toString())
          .addHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE)
          .addParameter("project", project)
          .addParameter("columnName", column)
          .addParameter("config", configuration.set("type", columnType.asJson()).toString())
          .addParameter("engine", ENGINE.toString())
          .addParameter(Constants.CSRF_TOKEN, token)
          .build();

      return client.execute(request, this);
    } catch (IOException ioe) {
      throw new RefineException(
          "Failed to perform reconciliation on project: '%s' due to: %s",
          project,
          ioe.getMessage());
    }
  }

  @Override
  public ReconcileCommandResponse handleResponse(HttpResponse response) throws IOException {
    HTTP_PARSER.assureStatusCode(response, HttpStatus.SC_OK);
    JsonNode node = JSON_PARSER.parseJson(response.getEntity().getContent());
    String code = JSON_PARSER.findExistingPath(node, "code").asText();
    switch (code) {
      case "ok":
        return ReconcileCommandResponse.ok();
      case "pending":
        return ReconcileCommandResponse.pending();
      case "error":
        String message = JSON_PARSER.findExistingPath(node, "message").asText();
        return ReconcileCommandResponse.error(message);
      default:
        throw new RefineException(
            "Failed to reconcile column: '%s' for project: '%s'", column, project);
    }
  }

  /**
   * Builder for {@link ReconcileCommand}.
   *
   * @author Antoniy Kunchev
   */
  public static class Builder {

    private String project;
    private String column;
    private ColumnType columnType;
    private String token;

    public Builder setProject(String project) {
      this.project = project;
      return this;
    }

    public Builder setColumn(String column) {
      this.column = column;
      return this;
    }

    public Builder setColumnType(ColumnType columnType) {
      this.columnType = columnType;
      return this;
    }

    public Builder setToken(String token) {
      this.token = token;
      return this;
    }

    /**
     * Builds the {@link ReconcileCommand}.
     *
     * @return a command
     */
    public ReconcileCommand build() {
      notBlank(project, "Missing 'project' argument");
      notBlank(column, "Missing 'column' argument");
      notNull(columnType, "Missing 'column type' argument");
      notBlank(token, "Missing CSRF token");
      return new ReconcileCommand(project, column, token, columnType);
    }
  }

  /**
   * Represents the guessed type of the column.
   *
   * @author Antoniy Kunchev
   */
  public static class ColumnType {

    private final String id;
    private final String name;

    /**
     * Creates new type.
     *
     * @param id of the type
     * @param name of the type
     */
    public ColumnType(String id, String name) {
      this.id = notBlank(id, "Missing 'id' argument in type config");
      this.name = notBlank(name, "Missing 'name' argument in type config");
    }

    private JsonNode asJson() {
      return JsonNodeFactory.instance.objectNode().put("id", id).put("name", name);
    }
  }
}
