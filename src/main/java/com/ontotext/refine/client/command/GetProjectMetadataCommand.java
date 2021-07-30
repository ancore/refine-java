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
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

import com.ontotext.refine.client.ProjectMetadata;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


/**
 * A command to retrieve project meta data.
 */
public class GetProjectMetadataCommand implements RefineCommand<GetProjectMetadataResponse> {

  private final String projectId;

  private GetProjectMetadataCommand(String projectId) {
    this.projectId = projectId;
  }

  @Override
  public String endpoint() {
    return "/orefine/command/core/get-project-metadata";
  }

  @Override
  public GetProjectMetadataResponse execute(RefineClient client) throws RefineException {
    try {
      HttpUriRequest request = RequestBuilder
          .get(client.createUrl(endpoint()).toString())
          .setHeader(ACCEPT, APPLICATION_JSON.getMimeType())
          .addParameter(new BasicNameValuePair("project", projectId))
          .build();

      return client.execute(request, this);
    } catch (IOException ioe) {
      throw new RefineException("Failed to get the metadata of the project: %s", projectId);
    }
  }

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
     * Builds the command after validation.
     *
     * @return the command
     */
    public GetProjectMetadataCommand build() {
      notBlank(projectId, "projectId is blank");
      return new GetProjectMetadataCommand(projectId);
    }
  }
}
