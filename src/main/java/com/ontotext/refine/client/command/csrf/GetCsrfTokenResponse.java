package com.ontotext.refine.client.command.csrf;


/**
 * This class represents the response from the {@link GetCsrfTokenCommand}.
 */
public class GetCsrfTokenResponse {

  private final String token;

  /**
   * Constructor.
   *
   * @param token the CSRF token
   */
  GetCsrfTokenResponse(String token) {
    this.token = token;
  }

  /**
   * Returns the CSRF token.
   *
   * @return the CSRF token
   */
  public String getToken() {
    return token;
  }

  @Override
  public String toString() {
    return getToken();
  }
}
