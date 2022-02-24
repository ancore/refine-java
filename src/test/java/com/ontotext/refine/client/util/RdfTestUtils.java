package com.ontotext.refine.client.util;

import java.io.InputStream;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

/**
 * Test utility that contains RDF data processing logic.
 *
 * @author Antoniy Kunchev
 */
public class RdfTestUtils {

  private RdfTestUtils() {
    // test utility
  }

  /**
   * Compares given RDF data streams. Useful when there are generated blank nodes.<br>
   * The method consumes the provided streams.
   *
   * @param expected path to a file containing the expected data
   * @param actual path to a file containing the actual data
   * @param format of the data
   * @return <code>true</code> if the files are producing the same semantic data, <code>false</code>
   *         otherwise
   * @throws AssertionError when error occurs during the comparison process
   */
  public static boolean compareAsRdf(InputStream expected, InputStream actual, RDFFormat format)
      throws AssertionError {
    try (InputStream eis = expected; InputStream ais = actual) {
      Model expectedModel = Rio.parse(expected, "", format);

      Model actualModel = Rio.parse(actual, "", format);
      return Models.isomorphic(expectedModel, actualModel);
    } catch (Exception exc) {
      throw new AssertionError("Failed to compare the provided files due to: " + exc.getMessage());
    }
  }

  /**
   * Compares given RDF data files. Useful when there are generated blank nodes.
   *
   * @param expected path to a file containing the expected data
   * @param actual path to a file containing the actual data
   * @param format of the data
   * @return <code>true</code> if the files are producing the same semantic data, <code>false</code>
   *         otherwise
   * @throws AssertionError when error occurs during the comparison process
   */
  public static boolean compareAsRdf(String expected, String actual, RDFFormat format)
      throws AssertionError {
    try (InputStream expectedIs = getResource(expected);
        InputStream actualIs = getResource(actual)) {
      return compareAsRdf(expectedIs, actualIs, format);
    } catch (Exception exc) {
      throw new AssertionError("Failed to compare the provided files due to: " + exc.getMessage());
    }
  }

  private static InputStream getResource(String expected) {
    return RdfTestUtils.class.getClassLoader().getResourceAsStream(expected);
  }
}
