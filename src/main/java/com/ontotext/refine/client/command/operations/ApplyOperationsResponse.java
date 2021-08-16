/*
 * Copyright 2019 DTAP GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ontotext.refine.client.command.operations;

import static org.apache.http.util.Asserts.notEmpty;

import com.ontotext.refine.client.RefineResponse;
import com.ontotext.refine.client.ResponseCode;


/**
 * This class represents the response from the <code>apply operations</code> request.
 */
public class ApplyOperationsResponse extends RefineResponse {

  /**
   * Private constructor to enforce usage of factory methods.
   *
   * @param code the code
   * @param message the message, may be {@code null}
   */
  private ApplyOperationsResponse(ResponseCode code, String message) {
    super(code, message);
  }

  /**
   * Returns an instance to represent the success status.
   *
   * @return the success instance
   */
  static ApplyOperationsResponse ok() {
    return new ApplyOperationsResponse(ResponseCode.OK, null);
  }


  /**
   * Returns an instance to represent the pending status.
   *
   * @return the pending instance
   */
  static ApplyOperationsResponse pending() {
    return new ApplyOperationsResponse(ResponseCode.PENDING, null);
  }

  /**
   * Returns an instance to represent an error.
   *
   * @param message the error message
   * @return the error instance
   */
  static ApplyOperationsResponse error(String message) {
    notEmpty(message, "The message should not be empty.");
    return new ApplyOperationsResponse(ResponseCode.ERROR, message);
  }
}
