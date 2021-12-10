package com.ontotext.refine.client.command;

import static org.apache.http.util.Asserts.notEmpty;
import static org.apache.http.util.Asserts.notNull;

import com.ontotext.refine.client.RefineResponse;
import com.ontotext.refine.client.ResponseCode;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the response from the <code>expression preview</code> request.
 */
public class ExpressionPreviewResponse extends RefineResponse {

  private List<String> expressionPreviews;

  /**
   * Private constructor to enforce usage of factory methods.
   *
   * @param code the code
   * @param message the message, may be {@code null}
   * @param expressionPreviews the expression previews, may be empty but not {@code null}
   */
  private ExpressionPreviewResponse(ResponseCode code, String message,
      List<String> expressionPreviews) {
    super(code, message);
    this.expressionPreviews = expressionPreviews;
  }

  /**
   * Returns an instance to represent a success.
   *
   * @param expressionPreviews the list of expression previews
   * @return the successful instance
   */
  static ExpressionPreviewResponse ok(List<String> expressionPreviews) {
    notNull(expressionPreviews, "expressionPreviews");
    return new ExpressionPreviewResponse(ResponseCode.OK, null, expressionPreviews);
  }

  /**
   * Returns an instance to represent an error.
   *
   * @param message the error message
   * @return the error instance
   */
  static ExpressionPreviewResponse error(String message) {
    notEmpty(message, "message");
    return new ExpressionPreviewResponse(ResponseCode.ERROR, message, Collections.emptyList());
  }

  /**
   * Returns the results if response is successful.
   *
   * @return the list of the expression previews, may be empty but not {@code null}
   */
  public List<String> getExpressionPreviews() {
    return expressionPreviews;
  }

  @Override
  public String toString() {
    return "ExpressionPreviewResponse{" + "expressionPreviews=" + expressionPreviews + '}';
  }
}
