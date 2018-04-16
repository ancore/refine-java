package gmbh.dtap.refine.client;

import java.util.List;

import static org.apache.http.util.Asserts.notEmpty;
import static org.apache.http.util.Asserts.notNull;

/**
 * This class represents the response from the <tt>expression preview</tt> request.
 *
 * @since 0.1.5
 */
class ExpressionPreviewResponse {

   private final boolean successful;
   private final String message;
   private List<String> expressionPreviews;

   private ExpressionPreviewResponse(boolean successful, String message, List<String> expressionPreviews) {
      this.successful = successful;
      this.message = message;
      this.expressionPreviews = expressionPreviews;
   }

   /**
    * Returns an instance to represent a success.
    *
    * @param expressionPreviews the list of expression previews
    * @return the successful instance
    * @since 0.1.5
    */
   static ExpressionPreviewResponse ok(List<String> expressionPreviews) {
      notNull(expressionPreviews, "expressionPreviews");
      return new ExpressionPreviewResponse(true, null, expressionPreviews);
   }

   /**
    * Returns an instance to represent an error.
    *
    * @param message the error message
    * @return the error instance
    * @since 0.1.5
    */
   static ExpressionPreviewResponse error(String message) {
      notEmpty(message, "message");
      return new ExpressionPreviewResponse(false, message, null);
   }

   /**
    * Indicates whether the request was successful.
    *
    * @return boolean
    * @since 0.1.5
    */
   boolean isSuccessful() {
      return successful;
   }

   /**
    * Returns the error message.
    *
    * @return the error message, {@code null} in success case
    * @since 0.1.5
    */
   String getMessage() {
      return message;
   }

   /**
    * Returns the results if response is successful.
    *
    * @return the list of the expression previews,
    * {@code null} if {@link #isSuccessful()} returns {@code false}
    */
   List<String> getExpressionPreviews() {
      return expressionPreviews;
   }

   @Override
   public String toString() {
      return "ExpressionPreviewResponse{" +
            "successful=" + successful +
            ", message='" + message + '\'' +
            ", expressionPreviews=" + expressionPreviews +
            '}';
   }
}
