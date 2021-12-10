package com.ontotext.refine.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;


/**
 * Unit Tests for {@link RefineResponse}.
 */
class RefineResponseTest {

  /**
   * Test implementation for abstract RefineResponse.
   */
  class TestRefineResponse extends RefineResponse {
    TestRefineResponse(ResponseCode code, String message) {
      super(code, message);
    }
  }

  @Test
  void should_have_fields_from_constructor() {
    RefineResponse refineResponse = new TestRefineResponse(ResponseCode.OK, "Test");
    assertEquals(ResponseCode.OK, refineResponse.getCode());
    assertEquals("Test", refineResponse.getMessage());
  }

  @Test
  void should_have_null_fields_from_constructor() {
    RefineResponse refineResponse = new TestRefineResponse(null, null);
    assertNull(refineResponse.getCode());
    assertNull(refineResponse.getMessage());
  }
}
