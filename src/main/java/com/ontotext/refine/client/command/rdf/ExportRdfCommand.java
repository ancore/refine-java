package com.ontotext.refine.client.command.rdf;

import static org.apache.commons.lang3.Validate.notBlank;

import com.fasterxml.jackson.databind.JsonNode;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommand;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.command.operations.GetOperationsResponse;
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

  private ExportRdfCommand(String project) {
    this.project = project;
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
      entity.setContent(
          IOUtils.toInputStream(getRdfMapping(client).asText(), StandardCharsets.UTF_8));

      HttpUriRequest request = RequestBuilder
          .post(client.createUrl(endpoint() + ":" + project).toString()).setEntity(entity).build();

      return client.execute(request, this);
    } catch (RefineException re) {
      throw re;
    } catch (IOException ioe) {
      String err = String.format(
          "Export of RDF data failed for project: '%s' due to: %s",
          project,
          ioe.getMessage());
      throw new RefineException(err);
    }
  }

  private JsonNode getRdfMapping(RefineClient client) throws RefineException, IOException {
    GetOperationsResponse response =
        RefineCommands.getOperations().setProject(project).build().execute(client);

    JsonNode mapping = response.getContent().findValue("mapping");

    if (mapping == null || mapping.isNull() || mapping.isMissingNode()) {
      throw new RefineException(
          String.format("Failed to retrieve the mapping for project: '%s'", project));
    }

    return mapping;
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

    public Builder setProject(String project) {
      this.project = project;
      return this;
    }

    /**
     * Builds a {@link ExportRdfCommand}.
     *
     * @return a command
     */
    public ExportRdfCommand build() {
      notBlank(project, "Missing 'project' argument");
      return new ExportRdfCommand(project);
    }
  }
}
