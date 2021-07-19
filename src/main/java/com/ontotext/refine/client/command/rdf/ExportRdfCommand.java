package com.ontotext.refine.client.command.rdf;

import static org.apache.commons.lang3.Validate.notBlank;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommand;
import com.ontotext.refine.client.exceptions.RefineException;
import com.ontotext.refine.client.util.HttpParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;


/**
 * A command that exports the data of specific project in RDF format.
 *
 * @author Antoniy Kunchev
 */
public class ExportRdfCommand implements RefineCommand<ExportRdfResponse> {

  private final String project;
  private final String mapping;

  private ExportRdfCommand(String project, String mapping) {
    this.project = project;
    this.mapping = mapping;
  }

  @Override
  public String endpoint() {
    return "/rest/rdf-mapper/rdf/ontorefine";
  }

  @Override
  public ExportRdfResponse execute(RefineClient client) throws RefineException {
    try {
      BasicHttpEntity entity = new BasicHttpEntity();
      entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
      entity.setContentEncoding(ContentType.APPLICATION_JSON.getCharset().toString());
      entity.setContent(IOUtils.toInputStream(mapping, StandardCharsets.UTF_8));

      HttpUriRequest request = RequestBuilder
          .post(client.createUrl(endpoint() + ":" + project).toString()).setEntity(entity).build();

      return client.execute(request, this);
    } catch (IOException ioe) {
      throw new RefineException(
          "Export of RDF data failed for project: '%s' due to: %s",
          project,
          ioe.getMessage());
    }
  }

  @Override
  public ExportRdfResponse handleResponse(HttpResponse response)
      throws ClientProtocolException, IOException {
    HttpParser.HTTP_PARSER.assureStatusCode(response, HttpStatus.SC_OK);
    return new ExportRdfResponse()
        .setProject(project)
        .setResultStream(EntityUtils.toString(response.getEntity()));
  }

  /**
   * Builder for {@link ExportRdfCommand}.
   *
   * @author Antoniy Kunchev
   */
  public static class Builder {

    private String project;
    private String mapping;

    public Builder setProject(String project) {
      this.project = project;
      return this;
    }

    public Builder setMapping(String mapping) {
      this.mapping = mapping;
      return this;
    }

    /**
     * Builds a {@link ExportRdfCommand}.
     *
     * @return a command
     */
    public ExportRdfCommand build() {
      notBlank(project, "Missing 'project' argument");
      notBlank(mapping, "Missing 'mapping' argument");
      return new ExportRdfCommand(project, mapping);
    }
  }
}
