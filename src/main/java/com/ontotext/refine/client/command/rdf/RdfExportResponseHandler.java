package com.ontotext.refine.client.command.rdf;

import static com.ontotext.refine.client.util.HttpParser.HTTP_PARSER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.HttpStatus.SC_OK;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for providing handling of the {@link HttpResponse} from the RDF export commands.
 *
 * @author Antoniy Kunchev
 */
class RdfExportResponseHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(RdfExportResponseHandler.class);

  private RdfExportResponseHandler() {
    throw new UnsupportedOperationException("Utility classes should not be instantiated.");
  }

  /**
   * Handles the response from RDF export command.
   *
   * @param project identifier which data was exported
   * @param format in which the data was exported. Provides the settings for the
   *        {@link ResultFormat.ResultType}
   * @param response that was returned from the request execution
   * @return {@link ExportRdfResponse} object containing the result from the export command
   * @throws IOException when there is a problem during the response handling
   */
  static ExportRdfResponse handle(String project, ResultFormat format, HttpResponse response)
      throws IOException {
    HTTP_PARSER.assureStatusCode(response, SC_OK);
    try (InputStream stream = response.getEntity().getContent()) {
      ExportRdfResponse rdfResponse = new ExportRdfResponse().setProject(project);
      switch (format.getAs()) {
        case STRING:
          return toStr(stream, rdfResponse);
        case FILE:
          return toFile(stream, rdfResponse);
        case STREAM:
          return rdfResponse.setResultStream(IOUtils.toBufferedInputStream(stream));
        default:
          return toStr(stream, rdfResponse);
      }
    }
  }

  private static ExportRdfResponse toStr(InputStream stream, ExportRdfResponse response)
      throws IOException {
    return response.setResult(IOUtils.toString(stream, UTF_8));
  }

  private static ExportRdfResponse toFile(InputStream stream, ExportRdfResponse response)
      throws IOException {
    Path tempDirectory = Files.createTempDirectory("ontorefine-client");
    String suffix = "project-" + response.getProject() + "-rdfExport-";
    File tempFile = Files.createTempFile(tempDirectory, suffix, ".tmp").toFile();
    LOGGER.trace("Writing the exported RDF in file: {}", tempFile.getAbsolutePath());
    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
      IOUtils.copyLarge(stream, fos);
    }
    return response.setResultFile(tempFile);
  }
}
