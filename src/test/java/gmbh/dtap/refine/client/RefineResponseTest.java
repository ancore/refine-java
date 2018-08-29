package gmbh.dtap.refine.client;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Tests for {@link RefineResponse}.
 */
public class RefineResponseTest {

   /**
    * Test implementation for abstract RefineResponse.
    */
   class TestRefineResponse extends RefineResponse {
      TestRefineResponse(ResponseCode code, String message) {
         super(code, message);
      }
   }

   @Test
   public void should_have_fields_from_constructor() {
      RefineResponse refineResponse = new TestRefineResponse(ResponseCode.OK, "Test");
      assertThat(refineResponse.getCode()).isEqualTo(ResponseCode.OK);
      assertThat(refineResponse.getMessage()).isEqualTo("Test");
   }

   @Test
   public void should_have_null_fields_from_constructor() {
      RefineResponse refineResponse = new TestRefineResponse(null, null);
      assertThat(refineResponse.getCode()).isNull();
      assertThat(refineResponse.getMessage()).isNull();
   }
}
