package gmbh.dtap.refine.client;

/**
 * This class represents the response from the delete project request.
 *
 * @since 0.1.0
 */
public class DeleteProjectResponse {

   private final boolean successful;
   private final String message;

   private DeleteProjectResponse(boolean successful, String message) {
      this.successful = successful;
      this.message = message;
   }

   public static DeleteProjectResponse ok() {
      return new DeleteProjectResponse(true, null);
   }

   public static DeleteProjectResponse error(String messages) {
      return new DeleteProjectResponse(false, messages);
   }

   public boolean isSuccessful() {
      return successful;
   }

   public String getMessage() {
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
