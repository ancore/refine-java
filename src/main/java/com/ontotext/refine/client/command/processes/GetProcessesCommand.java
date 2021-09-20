package com.ontotext.refine.client.command.processes;

import static org.apache.commons.lang3.Validate.notBlank;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommand;
import com.ontotext.refine.client.command.processes.GetProcessesCommandResponse.ProjectProcess;
import com.ontotext.refine.client.exceptions.RefineException;
import com.ontotext.refine.client.util.HttpParser;
import com.ontotext.refine.client.util.JsonParser;
import java.io.IOException;
import java.util.Collection;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;


/**
 * A command for retrieving the processes that are currently running for project.
 *
 * @author Antoniy Kunchev
 */
public class GetProcessesCommand implements RefineCommand<GetProcessesCommandResponse> {

  private static final TypeReference<Collection<ProjectProcess>> PROCESSES_TYPE =
      new TypeReference<Collection<ProjectProcess>>() {};

  private final String project;

  private GetProcessesCommand(String project) {
    this.project = project;
  }

  @Override
  public String endpoint() {
    return "/orefine/command/core/get-processes";
  }

  @Override
  public GetProcessesCommandResponse execute(RefineClient client) throws RefineException {
    try {
      String url = client.createUrl(endpoint()).toString();

      HttpUriRequest request =
          RequestBuilder.get(url).addParameter(Constants.PROJECT, project).build();

      return client.execute(request, this);
    } catch (IOException ioe) {
      throw new RefineException(
          "Failed to retrieve the processes for project: '%s' due to: %s",
          project,
          ioe.getMessage());
    }
  }

  @Override
  public GetProcessesCommandResponse handleResponse(HttpResponse response) throws IOException {
    HttpParser.HTTP_PARSER.assureStatusCode(response, HttpStatus.SC_OK);
    JsonNode json = JsonParser.JSON_PARSER.parseJson(response.getEntity().getContent());
    Collection<ProjectProcess> processes =
        JsonParser.JSON_PARSER.read(json.findValue("processes").toString(), PROCESSES_TYPE);
    return new GetProcessesCommandResponse(processes);
  }

  /**
   * Builder for {@link GetProcessesCommand}.
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
     * Builds the {@link GetProcessesCommand} after verifying that all arguments are available.
     *
     * @return the command
     */
    public GetProcessesCommand build() {
      notBlank(project, "Missing 'project' argument");
      return new GetProcessesCommand(project);
    }
  }
}
