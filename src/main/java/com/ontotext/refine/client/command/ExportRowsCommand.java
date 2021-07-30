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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;


/**
 * A command to export rows from the project.
 */
public class ExportRowsCommand implements RefineCommand<ExportRowsResponse> {

  private final String project;
  private final String engine;
  private final String format;
  private final String token;

  private ExportRowsCommand(String project, String engine, String format, String token) {
    this.project = project;
    this.engine = engine;
    this.format = format;
    this.token = token;
  }

  @Override
  public String endpoint() {
    return "/orefine/command/core/export-rows";
  }

  @Override
  public ExportRowsResponse execute(RefineClient client) throws RefineException {
    try {

      List<NameValuePair> form = new ArrayList<>();
      form.add(new BasicNameValuePair("project", project));
      form.add(new BasicNameValuePair("format", format));
      form.add(new BasicNameValuePair("engine", engine));
      form.add(new BasicNameValuePair("csrf_token", token));

      HttpUriRequest request = RequestBuilder
          .post(client.createUrl(endpoint()).toString())
          .setHeader(ACCEPT, APPLICATION_JSON.getMimeType())
          .setEntity(new UrlEncodedFormEntity(form, Consts.UTF_8))
          .build();

      return client.execute(request, this);
    } catch (IOException ioe) {
      throw new RefineException(
          "Failed to export data for project: '%s' due to: '%s'",
          project,
          ioe.getMessage());
    }
  }

  @Override
  public ExportRowsResponse handleResponse(HttpResponse response) throws IOException {
    File file = new File(new Date().getTime() + "." + format);
    try (FileOutputStream fos = new FileOutputStream(file);
        InputStream is = response.getEntity().getContent()) {
      int read = 0;
      byte[] buffer = new byte[32768];
      while ((read = is.read(buffer)) > 0) {
        fos.write(buffer, 0, read);
      }
      return new ExportRowsResponse(file);
    }
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
