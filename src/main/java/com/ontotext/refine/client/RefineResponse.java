package com.ontotext.refine.client;

/**
 * This class contains fields from a Refine server response JSON document.
 */
public abstract class RefineResponse {

  private final ResponseCode code;
  private final String message;

  /**
   * Constructor.
   *
   * @param code the code field
   * @param message the message field, may be {@code null}
   */
  protected RefineResponse(ResponseCode code, String message) {
    this.code = code;
    this.message = message;
  }

  /**
   * Returns the code from the response document.
   *
   * @return the code
   */
  public ResponseCode getCode() {
    return code;
  }

  /**
   * Returns the error message from the response document.
   *
   * @return the error message, may be {@code null}
   */
  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
    sb.append("{");
    sb.append("code=").append(code);
    if (message != null) {
      sb.append(", message='").append(message).append('\'');
    }
    sb.append('}');
    return sb.toString();
  }
}
