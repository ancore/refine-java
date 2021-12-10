package com.ontotext.refine.client.util;

import com.ontotext.refine.client.exceptions.RefineException;
import org.apache.http.HttpResponse;


/**
 * Provides utility logic for HTTP response parsing and validation.
 */
public enum HttpParser {

  HTTP_PARSER;

  /**
   * Validates that given {@link HttpResponse} has specific status code.
   *
   * @param response to be checked
   * @param statusCode that is expected
   * @throws RefineException when the expected status code is not matching the one in the response
   */
  public void assureStatusCode(HttpResponse response, int statusCode) throws RefineException {
    if (response.getStatusLine().getStatusCode() != statusCode) {
      throw new RefineException("Unexpected response : " + response.getStatusLine());
    }
  }
}
