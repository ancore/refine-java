/*
 * MIT License
 *
 * Copyright (c) 2018-2020 DTAP GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.ontotext.refine.client.command.csrf;

import static com.ontotext.refine.client.util.HttpParser.HTTP_PARSER;
import static com.ontotext.refine.client.util.JsonParser.JSON_PARSER;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.JsonNode;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommand;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;

/**
 * A command to retrieve Refine server version information.
 */
public class GetCsrfTokenCommand implements RefineCommand<GetCsrfTokenResponse> {

  /**
   * Constructor for {@link Builder}.
   */
  private GetCsrfTokenCommand() {
    // empty
  }

  @Override
  public String endpoint() {
    return "/orefine/command/core/get-csrf-token";
  }

  @Override
  public GetCsrfTokenResponse execute(RefineClient client) throws RefineException {
    try {
      URL url = client.createUrl(endpoint());

      HttpUriRequest request = RequestBuilder
          .get(url.toString())
          .setHeader(ACCEPT, APPLICATION_JSON.getMimeType())
          .build();

      return client.execute(request, this);
    } catch (IOException ioe) {
      String error = String.format("Failed to retrieve CSRF token due to: %s", ioe.getMessage());
      throw new RefineException(error);
    }
  }

  @Override
  public GetCsrfTokenResponse handleResponse(HttpResponse response) throws IOException {
    HTTP_PARSER.assureStatusCode(response, SC_OK);
    String responseBody = EntityUtils.toString(response.getEntity());
    return parseGetCsrfTokenResponse(responseBody);
  }

  private GetCsrfTokenResponse parseGetCsrfTokenResponse(String json) throws IOException {
    JsonNode node = JSON_PARSER.parseJson(json);
    return new GetCsrfTokenResponse(JSON_PARSER.findExistingPath(node, "token").asText());
  }

  /**
   * The builder for {@link GetCsrfTokenCommand}.
   */
  public static class Builder {

    /**
     * Builds the command.
     *
     * @return the command
     */
    public GetCsrfTokenCommand build() {
      return new GetCsrfTokenCommand();
    }
  }
}
