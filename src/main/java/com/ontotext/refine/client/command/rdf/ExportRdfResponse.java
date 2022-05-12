package com.ontotext.refine.client.command.rdf;

import java.io.File;
import java.io.InputStream;

/**
 * Holds the result from export RDF commands.
 *
 * @author Antoniy Kunchev
 */
public class ExportRdfResponse {

  private String project;
  private String result;
  private InputStream resultStream;
  private File resultFile;

  public String getProject() {
    return project;
  }

  ExportRdfResponse setProject(String project) {
    this.project = project;
    return this;
  }

  public String getResult() {
    return result;
  }

  ExportRdfResponse setResult(String result) {
    this.result = result;
    return this;
  }

  public InputStream getResultStream() {
    return resultStream;
  }

  ExportRdfResponse setResultStream(InputStream resultStream) {
    this.resultStream = resultStream;
    return this;
  }

  public File getResultFile() {
    return resultFile;
  }

  ExportRdfResponse setResultFile(File resultFile) {
    this.resultFile = resultFile;
    return this;
  }
}
