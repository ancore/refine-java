package gmbh.dtap.refine;

import gmbh.dtap.refine.api.RefineClient;
import gmbh.dtap.refine.client.MinimalRefineClient;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Factory methods for {@link RefineClient} instances.
 *
 * @since 0.1.0
 */
public class RefineClients {

   /**
    * Created a new instance of a {@link RefineClient}.
    *
    * @param url the URL of the OpenRefine server
    * @return the refine client
    * @since 0.1.0
    */
   public static RefineClient create(String url) {
      try {
         return new MinimalRefineClient(new URL(url));
      } catch (MalformedURLException e) {
         throw new IllegalArgumentException(e.getMessage(), e);
      }
   }

   /**
    * Created a new instance of a {@link RefineClient}.
    *
    * @param url the URL of the OpenRefine server
    * @return the refine client
    * @since 0.1.0
    */
   public static RefineClient create(URL url) {
      return new MinimalRefineClient(url);
   }
}
