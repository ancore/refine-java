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
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.JsonNode;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.RefineException;
import java.io.IOException;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;

/**
 * A command to retrieve Refine server version information.
 */
public class GetVersionCommand implements ResponseHandler<GetVersionResponse> {

  /**
   * Constructor for {@link Builder}.
   */
  private GetVersionCommand() {
    // empty
  }

  /**
   * Executes the command.
   *
   * @param client the client to execute the command with
   * @return the result of the operation
   * @throws IOException in case of a connection problem
   * @throws RefineException in case the server responses with an error or is not understood
   */
  public GetVersionResponse execute(RefineClient client) throws IOException {
    URL url = client.createUrl("/command/core/get-version");

    HttpUriRequest request = RequestBuilder.get(url.toString())
        .setHeader(ACCEPT, APPLICATION_JSON.getMimeType()).build();

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
  public GetVersionResponse handleResponse(HttpResponse response) throws IOException {
    HTTP_PARSER.assureStatusCode(response, SC_OK);
    String responseBody = EntityUtils.toString(response.getEntity());
    GetVersionResponse getVersionResponse = parseGetVersionResponse(responseBody);
    return getVersionResponse;
  }

  GetVersionResponse parseGetVersionResponse(String json) throws IOException {
    JsonNode node = JSON_PARSER.parseJson(json);
    return new GetVersionResponse(JSON_PARSER.findExistingPath(node, "full_name").asText(),
        JSON_PARSER.findExistingPath(node, "full_version").asText(),
        JSON_PARSER.findExistingPath(node, "version").asText(),
        JSON_PARSER.findExistingPath(node, "revision").asText());
  }

  /**
   * The builder for {@link GetVersionCommand}.
   */
  public static class Builder {

    /**
     * Builds the command.
     *
     * @return the command
     */
    public GetVersionCommand build() {
      return new GetVersionCommand();
    }
  }
}
