/*
 * Copyright 2019 DTAP GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ontotext.refine.client.command;

import static com.ontotext.refine.client.util.HttpParser.HTTP_PARSER;
import static com.ontotext.refine.client.util.JsonParser.JSON_PARSER;
import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

import com.ontotext.refine.client.ProjectLocation;
import com.ontotext.refine.client.ProjectMetadata;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.RefineProject;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * A command to retrieve project meta data.
 */
public class GetProjectMetadataCommand implements ResponseHandler<GetProjectMetadataResponse> {

  private final String projectId;

  /**
   * Constructor for {@link Builder}.
   *
   * @param projectId the project ID
   */
  private GetProjectMetadataCommand(String projectId) {
    this.projectId = projectId;
  }

  /**
   * Executes the command after validation.
   *
   * @param client the client to execute the command with
   * @return the result of the command
   * @throws IOException in case of a connection problem
   * @throws RefineException in case the server responses with an error or is not understood
   */
  public GetProjectMetadataResponse execute(RefineClient client) throws IOException {
    URL url = client.createUrl("command/core/get-project-metadata");

    HttpUriRequest request =
        RequestBuilder.get(url.toString()).setHeader(ACCEPT, APPLICATION_JSON.getMimeType())
            .addParameter(new BasicNameValuePair("project", projectId)).build();

    return client.execute(request, this);
  }

  /**
   * Validates the response and extracts necessary data.
   *
   * @param response the response to extract data from
   * @return the response representation
   * @throws IOException in case of a connection problem
   * @throws RefineException in case the server responses with an unexpected status or is not
   *         understood
   */
  @Override
  public GetProjectMetadataResponse handleResponse(HttpResponse response) throws IOException {
    HTTP_PARSER.assureStatusCode(response, SC_OK);
    String responseBody = EntityUtils.toString(response.getEntity());
    return new GetProjectMetadataResponse(JSON_PARSER.read(responseBody, ProjectMetadata.class));
  }

  /**
   * The builder for {@link GetProjectMetadataCommand}.
   */
  public static class Builder {

    private String projectId;

    /**
     * Sets the project ID.
     *
     * @param projectId the project ID
     * @return the builder for fluent usage
     */
    public Builder project(String projectId) {
      this.projectId = projectId;
      return this;
    }

    /**
     * Sets the project ID from the project location.
     *
     * @param projectLocation the project location
     * @return the builder for fluent usage
     */
    public Builder project(ProjectLocation projectLocation) {
      notNull(projectLocation, "projectLocation");
      this.projectId = projectLocation.getId();
      return this;
    }

    /**
     * Sets the project ID from the project.
     *
     * @param project the project
     * @return the builder for fluent usage
     */
    public Builder project(RefineProject project) {
      notNull(project, "project");
      this.projectId = project.getId();
      return this;
    }

    /**
     * Builds the command after validation.
     *
     * @return the command
     */
    public GetProjectMetadataCommand build() {
      notNull(projectId, "projectId");
      notEmpty(projectId, "projectId is empty");
      return new GetProjectMetadataCommand(projectId);
    }
  }
}
