package com.ontotext.refine.client.command.rdf;

import org.eclipse.rdf4j.rio.RDFFormat;

/**
 * Contains values for the result format of the export RDF commands. Basically it is a proxy for the
 * {@link RDFFormat} values, which decouples the 'RDF4J' dependency from the client.
 *
 * @author Antoniy Kunchev
 */
public enum ResultFormat {

  RDFXML(RDFFormat.RDFXML),

  NTRIPLES(RDFFormat.NTRIPLES),

  TURTLE(RDFFormat.TURTLE),

  TURTLESTAR(RDFFormat.TURTLESTAR),

  N3(RDFFormat.N3),

  TRIX(RDFFormat.TRIX),

  TRIG(RDFFormat.TRIG),

  TRIGSTAR(RDFFormat.TRIGSTAR),

  BINARY(RDFFormat.BINARY),

  NQUADS(RDFFormat.NQUADS),

  JSONLD(RDFFormat.JSONLD),

  RDFJSON(RDFFormat.RDFJSON),

  RDFA(RDFFormat.RDFA),

  HDT(RDFFormat.HDT);

  private final RDFFormat rdfFormat;
  private ResultType as;

  private ResultFormat(RDFFormat format) {
    this.rdfFormat = format;
    this.as = ResultType.STRING;
  }

  public RDFFormat getRdfFormat() {
    return rdfFormat;
  }

  /**
   * Sets the expected result type.
   *
   * @param as the type of the result
   * @return current {@link ResultFormat} object
   */
  public ResultFormat as(ResultType as) {
    this.as = as;
    return this;
  }

  public ResultType getAs() {
    return as;
  }

  /**
   * Provides options for type of the result in which the exported data can be returned.
   *
   * @author Antoniy Kunchev
   */
  public enum ResultType {
    STRING, FILE, STREAM
  }
}
