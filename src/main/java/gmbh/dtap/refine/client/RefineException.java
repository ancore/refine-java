package gmbh.dtap.refine.client;

import java.io.IOException;

/**
 * This exception is thrown when the server responds with an error or unexpected response,
 * and when the response can not be understood.
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
