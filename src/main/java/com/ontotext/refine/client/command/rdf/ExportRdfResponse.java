package com.ontotext.refine.client.command.rdf;


/**
 * Holds the result from {@link ExportRdfCommand}.
 *
 * @author Antoniy Kunchev
 */
public class ExportRdfResponse {

  private String project;
  private String result;

  public String getProject() {
    return project;
  }

  public ExportRdfResponse setProject(String project) {
    this.project = project;
    return this;
  }

  public String getResult() {
    return result;
  }

  public ExportRdfResponse setResultStream(String result) {
    this.result = result;
    return this;
  }
}
