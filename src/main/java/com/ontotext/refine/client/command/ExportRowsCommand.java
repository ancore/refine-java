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

package com.ontotext.refine.client.command;

import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 * A command to export rows from the project.
 */
public class ExportRowsCommand implements ResponseHandler<ExportRowsResponse> {

  private final String project;
  private final String engine;
  private final String format;
  private final String token;

  /**
   * Constructor for {@link Builder}.
   *
   * @param project the project name
   * @param engine the engine to use
   * @param format the optional upload format
   * @param token the csrf token
   */
  private ExportRowsCommand(String project, String engine, String format, String token) {
    this.project = project;
    this.engine = engine;
    this.format = format;
    this.token = token;
  }

  /**
   * Executes the command.
   *
   * @param client the client to execute the command with
   * @return the result of the command
   * @throws IOException in case of a connection problem
   * @throws RefineException in case the server responses with an error or is not understood
   */
  public ExportRowsResponse execute(RefineClient client) throws IOException {
    final URL url = client.createUrl("/command/core/export-rows");

    List<NameValuePair> form = new ArrayList<>();
    form.add(new BasicNameValuePair("project", project));
    form.add(new BasicNameValuePair("format", format));
    form.add(new BasicNameValuePair("engine", engine));
    form.add(new BasicNameValuePair("csrf_token", token));
    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

    HttpUriRequest request = RequestBuilder.post(url.toString())
        .setHeader(ACCEPT, APPLICATION_JSON.getMimeType()).setEntity(entity).build();

    return client.execute(request, this);
  }

  /**
   * Validates the response and extracts necessary data.
   *
   * @param response the response to get the location from
   * @return the response
   * @throws IOException in case of an connection problem
   * @throws RefineException in case of an unexpected response or no location header is present
   */
  @Override
  public ExportRowsResponse handleResponse(HttpResponse response) throws IOException {
    // TODO: parse errors in refine are returned as HTML
    // HTTP_PARSER.assureStatusCode(response, SC_MOVED_TEMPORARILY);
    // Header location = response.getFirstHeader("Location");
    // if (location == null) {
    // throw new RefineException("No location header found.");
    // }
    // URL url = new URL(location.getValue());
    InputStream is = response.getEntity().getContent();
    File file = new File(new Date().getTime() + "." + format);
    FileOutputStream fos = new FileOutputStream(file);
    int read = 0;
    byte[] buffer = new byte[32768];
    while ((read = is.read(buffer)) > 0) {
      fos.write(buffer, 0, read);
    }
    fos.close();
    is.close();
    return new ExportRowsResponse(file);
  }

  /**
   * The builder for {@link ExportRowsCommand}.
   */
  public static class Builder {

    private String project;
    private String engine;
    private String format;
    private String token;

    /**
     * Sets the project name.
     *
     * @param project the project name
     * @return the builder for fluent usage
     */
    public Builder project(String project) {
      this.project = project;
      return this;
    }

    /**
     * Sets token.
     *
     * @param token the csrf token
     * @return the builder for fluent usage
     */
    public Builder token(String token) {
      this.token = token;
      return this;
    }

    /**
     * Sets the file containing the data to upload.
     *
     * @param engine the engine to use
     * @return the builder for fluent usage
     */
    public Builder engine(String engine) {
      this.engine = engine;
      return this;
    }

    /**
     * Sets the optional upload format.
     *
     * @param format the optional upload format
     * @return the builder for fluent usage
     */
    public Builder format(String format) {
      this.format = format;
      return this;
    }

    /**
     * Builds the command after validation.
     *
     * @return the command
     */
    public ExportRowsCommand build() {
      notNull(engine, "engine");
      notNull(project, "project");
      notNull(token, "token");
      notNull(format, "format");
      return new ExportRowsCommand(project, engine, format, token);
    }
  }
}
