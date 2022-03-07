package com.ontotext.refine.client.command.rdf;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommand;
import com.ontotext.refine.client.exceptions.RefineException;
import com.ontotext.refine.client.util.HttpParser;
import com.ontotext.refine.client.util.mappings.MappingsNormalizer;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.eclipse.rdf4j.rio.RDFFormat;

/**
 * A command that exports the data of specific project in RDF format using the mapping defined in
 * the Mapping UI.
 *
 * @author Antoniy Kunchev
 */
public class DefaultExportRdfCommand implements RefineCommand<ExportRdfResponse> {

  private final String project;
  private final String mapping;
  private final ResultFormat format;

  private DefaultExportRdfCommand(String project, String mapping, ResultFormat format) {
    this.project = project;
    this.mapping = mapping;
    this.format = format;
  }

  @Override
  public String endpoint() {
    return "/rest/rdf-mapper/rdf/ontorefine";
  }

  @Override
  public ExportRdfResponse execute(RefineClient client) throws RefineException {
    try {
      BasicHttpEntity entity = new BasicHttpEntity();
      entity.setContentType(APPLICATION_JSON.getMimeType());
      entity.setContentEncoding(APPLICATION_JSON.getCharset().toString());
      entity.setContent(IOUtils.toInputStream(MappingsNormalizer.forRdfExport(mapping), UTF_8));

      RDFFormat rdfFormat = format.getRdfFormat();
      String acceptHeader = rdfFormat.getDefaultMIMEType() + ";charset=" + rdfFormat.getCharset();

      HttpUriRequest request = RequestBuilder
          .post(client.createUri(endpoint() + ":" + project))
          .addHeader(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
          .addHeader(ACCEPT, acceptHeader)
          .setEntity(new BufferedHttpEntity(entity))
          .build();

      return client.execute(request, this);
    } catch (IOException ioe) {
      throw new RefineException(
          "Export of RDF data failed for project: '%s' due to: %s",
          project,
          ioe.getMessage());
    }
  }

  @Override
  public ExportRdfResponse handleResponse(HttpResponse response) throws IOException {
    HttpParser.HTTP_PARSER.assureStatusCode(response, HttpStatus.SC_OK);
    return new ExportRdfResponse()
        .setProject(project)
        .setResult(IOUtils.toString(response.getEntity().getContent(), UTF_8));
  }

  /**
   * Builder for {@link DefaultExportRdfCommand}.
   *
   * @author Antoniy Kunchev
   */
  public static class Builder {

    private String project;
    private String mapping;
    private ResultFormat format;

    public Builder setProject(String project) {
      this.project = project;
      return this;
    }

    public Builder setMapping(String mapping) {
      this.mapping = mapping;
      return this;
    }

    public Builder setFormat(ResultFormat format) {
      this.format = format;
      return this;
    }

    /**
     * Builds a {@link DefaultExportRdfCommand}.
     *
     * @return a command
     */
    public DefaultExportRdfCommand build() {
      notBlank(project, "Missing 'project' argument");
      notBlank(mapping, "Missing 'mapping' argument");
      notNull(format, "Missing 'format' argument");
      return new DefaultExportRdfCommand(project, mapping, format);
    }
  }
}
