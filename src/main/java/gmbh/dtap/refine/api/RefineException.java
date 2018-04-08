package gmbh.dtap.refine.api;

import java.io.IOException;

/**
 * This exception is thrown when the server responds with an error or unexpected response,
 * and when the response can not be understood.
 *
 * @since 0.1.3
 */
public class RefineException extends IOException {

   private static final long serialVersionUID = 5368187574797784277L;

   public RefineException(String message) {
      super(message);
   }

   public RefineException(String message, Throwable cause) {
      super(message, cause);
   }

   public RefineException(Throwable cause) {
      super(cause);
   }
}
