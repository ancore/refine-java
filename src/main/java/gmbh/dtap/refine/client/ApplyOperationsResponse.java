package gmbh.dtap.refine.client;

import static org.apache.http.util.Asserts.notEmpty;

/**
 * This class represents the response from the <tt>apply operations</tt> request.
 *
 * @since 0.1.4
 */
class ApplyOperationsResponse {

   private final boolean successful;
   private final String message;

   private ApplyOperationsResponse(boolean successful, String message) {
      this.successful = successful;
      this.message = message;
   }

   /**
    * Returns an instance to represent a success.
    *
    * @return the sucessful instance
    * @since 0.1.4
    */
   static ApplyOperationsResponse ok() {
      return new ApplyOperationsResponse(true, null);
   }

   /**
    * Returns an instance to represent an error.
    *
    * @param message the error message
    * @return the error instance
    * @since 0.1.4
    */
   static ApplyOperationsResponse error(String message) {
      notEmpty(message, "message");
      return new ApplyOperationsResponse(false, message);
   }

   /**
    * Indicates whether the request was successful.
    *
    * @return boolean
    * @since 0.1.4
    */
   boolean isSuccessful() {
      return successful;
   }

   /**
    * Returns the error message.
    *
    * @return the error message, {@code null} in success case
    * @since 0.1.4
    */
   String getMessage() {
      return message;
   }

   @Override
   public String toString() {
      return "ApplyOperationsResponse{" +
            "successful=" + successful +
            ", message='" + message + '\'' +
            '}';
   }
}
