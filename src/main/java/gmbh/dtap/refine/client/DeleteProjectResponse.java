package gmbh.dtap.refine.client;

import static org.apache.http.util.Asserts.notEmpty;

/**
 * This class represents the response from the <tt>delete project</tt> request.
 *
 * @since 0.1.0
 */
class DeleteProjectResponse {

   private final boolean successful;
   private final String message;

   private DeleteProjectResponse(boolean successful, String message) {
      this.successful = successful;
      this.message = message;
   }

   /**
    * Returns an instance to represent a success.
    *
    * @return the sucessful instance
    * @since 0.1.0
    */
   static DeleteProjectResponse ok() {
      return new DeleteProjectResponse(true, null);
   }

   /**
    * Returns an instance to represent an error.
    *
    * @param message the error message
    * @return the error instance
    * @since 0.1.0
    */
   static DeleteProjectResponse error(String message) {
      notEmpty(message, "message");
      return new DeleteProjectResponse(false, message);
   }

   /**
    * Indicates whether the request was successful.
    *
    * @return boolean
    * @since 0.1.0
    */
   boolean isSuccessful() {
      return successful;
   }

   /**
    * Returns the error message.
    *
    * @return the error message, {@code null} in success case
    * @since 0.1.0
    */
   String getMessage() {
      return message;
   }

   @Override
   public String toString() {
      return "DeleteProjectResponse{" +
            "successful=" + successful +
            ", message='" + message + '\'' +
            '}';
   }
}
