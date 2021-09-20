package com.ontotext.refine.client.command.rdf;

import org.eclipse.rdf4j.rio.RDFFormat;

/**
 * Contains values for the result format of the {@link ExportRdfCommand}. Basically it is a proxy
 * for the {@link RDFFormat} values, which decouples the 'RDF4J' dependency from the client.
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

  private ResultFormat(RDFFormat format) {
    this.rdfFormat = format;
  }

  public RDFFormat getRdfFormat() {
    return rdfFormat;
  }
}
