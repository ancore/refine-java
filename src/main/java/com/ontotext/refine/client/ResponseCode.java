package com.ontotext.refine.client;

/**
 * Represents the field <code>code</code> from a refine response.
 */
public enum ResponseCode {

  /**
   * Operation was successful.
   */
  OK,

  /**
   * Operation is pending.
   */
  PENDING,

  /**
   * Operation failed.
   */
  ERROR
}
