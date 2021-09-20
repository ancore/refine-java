package com.ontotext.refine.client.command.reconcile;

import static com.ontotext.refine.client.util.HttpParser.HTTP_PARSER;
import static com.ontotext.refine.client.util.JsonParser.JSON_PARSER;
import static org.apache.commons.lang3.Validate.notBlank;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommand;
import com.ontotext.refine.client.command.reconcile.GuessColumnTypeCommandResponse.ReconciliationType;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;


/**
 * A command that exports the data of specific project in RDF format.
 *
 * @author Antoniy Kunchev
 */
public class GuessColumnTypeCommand implements RefineCommand<GuessColumnTypeCommandResponse> {

  private static final TypeReference<List<ReconciliationType>> TYPES =
      new TypeReference<List<ReconciliationType>>() {};

  private final String project;
  private final String column;
  private final String service;
  private final String token;

  private GuessColumnTypeCommand(String project, String column, String service, String token) {
    this.project = project;
    this.column = column;
    this.service = service;
    this.token = token;
  }

  @Override
  public String endpoint() {
    return "/orefine/command/core/guess-types-of-column";
  }

  @Override
  public GuessColumnTypeCommandResponse execute(RefineClient client) throws RefineException {
    try {
      URL url = client.createUrl(endpoint());

      // TODO not good, but this might take a lot of time depending on the service
      RequestConfig config = RequestConfig
          .custom().setSocketTimeout(0).setConnectTimeout(0).setConnectionRequestTimeout(0).build();

      HttpUriRequest request = RequestBuilder
          .post(url.toString())
          .setConfig(config)
          .addParameter(Constants.PROJECT, project)
          .addParameter("columnName", column)
          .addParameter("service", service)
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
  public GuessColumnTypeCommandResponse handleResponse(HttpResponse response) throws IOException {
    HTTP_PARSER.assureStatusCode(response, HttpStatus.SC_OK);

    JsonNode node = JSON_PARSER.parseJson(response.getEntity().getContent());
    String code = JSON_PARSER.findExistingPath(node, "code").asText();

    if ("ok".equals(code)) {
      String typesAsStr = JSON_PARSER.findExistingPath(node, "types").toString();
      List<ReconciliationType> types = JSON_PARSER.read(typesAsStr, TYPES);
      return GuessColumnTypeCommandResponse.ok(project, column, types);
    }

    if ("error".equals(code)) {
      String message = JSON_PARSER.findExistingPath(node, "message").asText();
      return GuessColumnTypeCommandResponse.error(project, column, message);
    }

    throw new RefineException(
        "Failed to guess the type of the column: '%s' for project: '%s'", column, project);
  }

  /**
   * Builder for {@link GuessColumnTypeCommand}.
   *
   * @author Antoniy Kunchev
   */
  public static class Builder {

    private String project;
    private String column;
    private String service;
    private String token;

    public Builder setProject(String project) {
      this.project = project;
      return this;
    }

    public Builder setColumn(String column) {
      this.column = column;
      return this;
    }

    public Builder setService(String service) {
      this.service = service;
      return this;
    }

    public Builder setToken(String token) {
      this.token = token;
      return this;
    }

    /**
     * Builds the {@link GuessColumnTypeCommand}.
     *
     * @return a command
     */
    public GuessColumnTypeCommand build() {
      notBlank(project, "Missing 'project' argument");
      notBlank(column, "Missing 'column' argument");
      notBlank(service, "Missing 'service' argument");
      notBlank(token, "Missing CSRF token");
      return new GuessColumnTypeCommand(project, column, service, token);
    }
  }
}
