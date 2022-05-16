package com.ontotext.refine.client.command.rdf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Contains convenient methods for files processing during RDF export.
 *
 * @author Antoniy Kunchev
 */
class RdfExportFileUtils {

  private RdfExportFileUtils() {
    throw new UnsupportedOperationException("Utility class.");
  }

  /**
   * Creates temporary file with name pattern:<br>
   * <i>project-{project identifier}-rdfExport-{java generated number}.tmp</i><br>
   * in temporary directory prefixed with: <br>
   * <i>ontorefine-client-{java generated number}</i>.
   *
   * @param project the identifer of the refine project, which data is exported
   * @return path to the created file
   * @throws IOException when there is an error during the file creation
   */
  static Path createTempFile(String project) throws IOException {
    Path tempDirectory = Files.createTempDirectory("ontorefine-client-");
    String suffix = "project-" + project + "-rdfExport-";
    return Files.createTempFile(tempDirectory, suffix, ".tmp");
  }
}
