package com.ontotext.refine.client.command.rdf;


/**
 * Holds the result from export RDF commands.
 *
 * @author Antoniy Kunchev
 */
public class ExportRdfResponse {

  private String project;
  private String result;

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
}
