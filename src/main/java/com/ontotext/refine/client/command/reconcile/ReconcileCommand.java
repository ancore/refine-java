package com.ontotext.refine.client.command.reconcile;

import static org.apache.commons.lang3.Validate.notBlank;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommand;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;


/**
 * A command that performs reconciliation process over the project data.
 *
 * @author Antoniy Kunchev
 */
public class ReconcileCommand implements RefineCommand<ReconcileCommandResponse> {

  private final String project;

  private ReconcileCommand(String project) {
    this.project = project;
  }

  @Override
  public String endpoint() {
    return "/orefine/command/core/reconcile";
  }

  @Override
  public ReconcileCommandResponse execute(RefineClient client) throws RefineException {
    try {
      URL url = client.createUrl(endpoint() + "?project=" + project);
      // TODO build request body
      HttpUriRequest request = RequestBuilder.post(url.toString()).build();
      return client.execute(request, this);
    } catch (IOException ioe) {
      throw new RefineException(
          "Failed to perform reconciliation on project: '%s' due to: %s",
          project,
          ioe.getMessage());
    }
  }

  @Override
  public ReconcileCommandResponse handleResponse(HttpResponse response)
      throws ClientProtocolException, IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Builder for {@link ReconcileCommand}.
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
     * Builds the {@link ReconcileCommand}.
     *
     * @return a command
     */
    public ReconcileCommand build() {
      notBlank(project, "Missing 'project' argument");
      return new ReconcileCommand(project);
    }
  }
}
