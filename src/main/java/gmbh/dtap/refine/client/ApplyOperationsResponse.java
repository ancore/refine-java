package gmbh.dtap.refine.client;

import static org.apache.http.util.Asserts.notEmpty;

/**
 * This class represents the response from the <tt>apply operations</tt> request.
 */
public class ApplyOperationsResponse extends RefineResponse {

   /**
    * Private constructor to enforce usage of factory methods.
    *
    * @param code    the code
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
      notEmpty(message, "message");
      return new ApplyOperationsResponse(ResponseCode.ERROR, message);
   }
}
