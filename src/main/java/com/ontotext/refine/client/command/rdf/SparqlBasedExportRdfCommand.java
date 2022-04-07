package com.ontotext.refine.client.command.rdf;

import static com.ontotext.refine.client.util.HttpParser.HTTP_PARSER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.entity.ContentType.APPLICATION_FORM_URLENCODED;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommand;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A command that exports the data of specific project in RDF format using a SPARQL Construct query.
 * The query should be template containing specific placeholder for the project identifier. The
 * placeholder allows execution of the same query over multiple projects with same data
 * structure.<br>
 * The default placeholder set in the command is <code>#project_placeholder#</code>, but it can be
 * changed by providing different one, when building the command.
 *
 * @author Antoniy Kunchev
 */
public class SparqlBasedExportRdfCommand implements RefineCommand<ExportRdfResponse> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SparqlBasedExportRdfCommand.class);

  private final String project;
  private final String projectPlaceholder;
  private final String query;
  private final ResultFormat format;
  private final String repository;

  private SparqlBasedExportRdfCommand(
      String project,
      String projectPlaceholder,
      String query,
      ResultFormat format,
      String repository) {
    this.project = project;
    this.projectPlaceholder = projectPlaceholder;
    this.query = query;
    this.format = format;
    this.repository = repository;
  }

  @Override
  public String endpoint() {
    return "/repositories/{repo}";
  }

  @Override
  public ExportRdfResponse execute(RefineClient client) throws RefineException {
    try {
      RDFFormat rdfFormat = format.getRdfFormat();
      String acceptHeader = rdfFormat.getDefaultMIMEType() + ";charset=" + rdfFormat.getCharset();

      HttpUriRequest request = RequestBuilder
          .post(client.createUri(endpoint().replace("{repo}", repository)))
          .addHeader(ACCEPT, acceptHeader)
          .addHeader(CONTENT_TYPE, APPLICATION_FORM_URLENCODED.withCharset(UTF_8).toString())
          .setEntity(buildEntity())
          .build();

      return client.execute(request, this);
    } catch (IOException ioe) {
      throw new RefineException(
          "Export of RDF data failed for project: '%s' due to: %s",
          project,
          ioe.getMessage());
    }
  }

  /**
   * Builds the request entity with the expected content. The produced {@link HttpEntity} is
   * repeatable so that it can be retried.
   */
  private HttpEntity buildEntity() throws IOException {
    return new BufferedHttpEntity(new UrlEncodedFormEntity(buildRequestContent(), UTF_8));
  }

  /**
   * Replaces the placeholder for the project in the query template.<br>
   * Effectively:
   *
   * <pre>
   * SERVICE &lt;rdf-mapper:ontorefine:#project_placeholder#&gt;
   *
   * is transformed into
   *
   * SERVICE &lt;rdf-mapper:ontorefine:1958197932150&gt;
   * </pre>
   * And prepends the expected field for the request.
   */
  private List<NameValuePair> buildRequestContent() {
    String fixedQuery = query.replaceFirst(projectPlaceholder, project);
    LOGGER.debug("The query that will be used for RDF export is: {}", fixedQuery);
    return singletonList(new BasicNameValuePair("query", fixedQuery));
  }

  @Override
  public ExportRdfResponse handleResponse(HttpResponse response) throws IOException {
    HTTP_PARSER.assureStatusCode(response, SC_OK);
    return new ExportRdfResponse()
        .setProject(project)
        .setResult(IOUtils.toString(response.getEntity().getContent(), UTF_8));
  }

  /**
   * Builder for {@link SparqlBasedExportRdfCommand}.
   *
   * @author Antoniy Kunchev
   */
  public static class Builder {

    private String project;
    private String projectPlaceholder = "#project_placeholder#";
    private String query;
    private ResultFormat format;
    private String repository;

    public Builder setProject(String project) {
      this.project = project;
      return this;
    }

    public Builder setProjectPlaceholder(String projectPlaceholder) {
      this.projectPlaceholder = projectPlaceholder;
      return this;
    }

    public Builder setQuery(String query) {
      this.query = query;
      return this;
    }

    public Builder setFormat(ResultFormat format) {
      this.format = format;
      return this;
    }

    public Builder setRepository(String repository) {
      this.repository = repository;
      return this;
    }

    /**
     * Builds a {@link SparqlBasedExportRdfCommand}.
     *
     * @return a command
     */
    public SparqlBasedExportRdfCommand build() {
      notBlank(project, "Missing 'project' argument");
      notBlank(projectPlaceholder, "The 'projectPlaceholder' argument should not be blank");
      notBlank(query, "Missing 'query' argument");
      notNull(format, "Missing 'format' argument");
      notBlank(repository, "Missing 'repository' argument");
      return new SparqlBasedExportRdfCommand(
          project, projectPlaceholder, query, format, repository);
    }
  }
}
