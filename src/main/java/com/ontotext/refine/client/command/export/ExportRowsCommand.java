package com.ontotext.refine.client.command.export;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

import com.ontotext.refine.client.Options;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommand;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
  private final String options;
  private final String token;

  private ExportRowsCommand(
      String project, String engine, String format, String options, String token) {
    this.project = project;
    this.engine = engine;
    this.format = format;
    this.options = options;
    this.token = token;
  }

  @Override
  public String endpoint() {
    return "/orefine/command/core/export-rows";
  }

  @Override
  public ExportRowsResponse execute(RefineClient client) throws RefineException {
    try {

      List<NameValuePair> form = buildForm();

      HttpUriRequest request = RequestBuilder
          .post(client.createUrl(endpoint()).toString())
          .setHeader(ACCEPT, APPLICATION_JSON.getMimeType())
          .setEntity(new UrlEncodedFormEntity(form, UTF_8))
          .build();

      return client.execute(request, this);
    } catch (IOException ioe) {
      throw new RefineException(
          "Failed to export data for project: '%s' due to: '%s'",
          project,
          ioe.getMessage());
    }
  }

  private List<NameValuePair> buildForm() {
    List<NameValuePair> form = new ArrayList<>(5);
    form.add(new BasicNameValuePair(Constants.PROJECT, project));
    form.add(new BasicNameValuePair("format", format));
    form.add(new BasicNameValuePair("options", options));
    form.add(new BasicNameValuePair("engine", engine));
    form.add(new BasicNameValuePair(Constants.CSRF_TOKEN, token));
    return form;
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

    private static final Options DEFAULT_OPTIONS = () -> "{}";

    private String project;
    private Engines engine;
    private String format;
    private Options options;
    private String token;

    public Builder setProject(String project) {
      this.project = project;
      return this;
    }

    public Builder setEngine(Engines engine) {
      this.engine = engine;
      return this;
    }

    public Builder setFormat(String format) {
      this.format = format;
      return this;
    }

    public void setOptions(Options options) {
      this.options = options;
    }

    public Builder setToken(String token) {
      this.token = token;
      return this;
    }

    /**
     * Builds the command after validation.
     *
     * @return the command
     */
    public ExportRowsCommand build() {
      notBlank(project, "Missing 'project' argument");
      notBlank(format, "Missing 'format' argument");
      notBlank(token, "Missing CSRF token");
      options = defaultIfNull(options, DEFAULT_OPTIONS);
      engine = defaultIfNull(engine, Engines.ROW_BASED);
      return new ExportRowsCommand(project, engine.get(), format, options.asJson(), token);
    }
  }
}
