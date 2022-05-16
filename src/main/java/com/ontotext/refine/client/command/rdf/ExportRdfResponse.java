package com.ontotext.refine.client.command.rdf;

import static com.ontotext.refine.client.command.rdf.RdfExportFileUtils.createTempFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.apache.commons.lang3.StringUtils;

/**
 * Holds the result from export RDF commands.
 *
 * @author Antoniy Kunchev
 */
public class ExportRdfResponse {

  private String project;
  private String result;
  private File resultFile;

  public String getProject() {
    return project;
  }

  ExportRdfResponse setProject(String project) {
    this.project = project;
    return this;
  }

  public String getResult() throws IOException {
    return resultFile == null ? result : Files.readString(resultFile.toPath());
  }

  ExportRdfResponse setResult(String result) {
    this.result = result;
    return this;
  }

  /**
   * Retrieves the result as stream. The result is returned either as {@link ByteArrayInputStream}
   * or {@link FileInputStream} depending on whether it was buffered in-memory or written in a file.
   *
   * @return stream of the result
   * @throws IOException when error occurs during the result streaming
   */
  public InputStream getResultStream() throws IOException {
    return resultFile == null
        ? new ByteArrayInputStream(result.getBytes())
        : new FileInputStream(resultFile);
  }

  /**
   * Retrieves the result as file.
   *
   * @return file containing the result from the RDF export
   * @throws IOException when error occurs during file IO operations
   */
  public File getResultFile() throws IOException {
    if (resultFile == null && StringUtils.isNotBlank(result)) {
      resultFile = Files.write(createTempFile(project), result.getBytes()).toFile();
    }
    return resultFile;
  }

  ExportRdfResponse setResultFile(File resultFile) {
    this.resultFile = resultFile;
    return this;
  }
}
