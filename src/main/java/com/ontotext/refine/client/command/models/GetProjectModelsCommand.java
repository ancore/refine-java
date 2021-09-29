package com.ontotext.refine.client.command.models;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommand;
import com.ontotext.refine.client.exceptions.RefineException;
import com.ontotext.refine.client.util.HttpParser;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

/**
 * A command that retrieves the models for specified project.
 *
 * @author Antoniy Kunchev
 */
public class GetProjectModelsCommand implements RefineCommand<GetProjectModelsResponse> {

  private final String project;

  private GetProjectModelsCommand(String project) {
    this.project = project;
  }

  @Override
  public String endpoint() {
    return "/orefine/command/core/get-models";
  }

  @Override
  public GetProjectModelsResponse execute(RefineClient client) throws RefineException {
    try {
      URL url = client.createUrl(endpoint());
      HttpUriRequest request =
          RequestBuilder.get(url.toString()).addParameter(Constants.PROJECT, project).build();
      return client.execute(request, this);
    } catch (IOException ioe) {
      throw new RefineException(
          "Failed to retrieve the models for project: '%s' due to: %s",
          project,
          ioe.getMessage());
    }
  }

  @Override
  public GetProjectModelsResponse handleResponse(HttpResponse response) throws IOException {
    HttpParser.HTTP_PARSER.assureStatusCode(response, HttpStatus.SC_OK);
    return new JsonMapper()
        .readValue(response.getEntity().getContent(), GetProjectModelsResponse.class);
  }

  /**
   * Builder for {@link GetProjectModelsCommand}.
   *
   * @author Antoniy Kunchev
   */
  public static class Builder {

    private String project;

    public Builder setProject(String project) {
      this.project = project;
      return this;
    }

    /**
     * Builds the {@link GetProjectModelsCommand}.
     *
     * @return a command
     */
    public GetProjectModelsCommand build() {
      Validate.notBlank(project, "Missing 'project' argument");
      return new GetProjectModelsCommand(project);
    }
  }
}