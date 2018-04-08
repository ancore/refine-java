package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.RefineProjectLocation;

import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.lang.StringUtils.substringAfterLast;
import static org.apache.http.util.Asserts.notEmpty;
import static org.apache.http.util.Asserts.notNull;

/**
 * A minimal implementation of {@link RefineProjectLocation}.
 *
 * @since 0.1.3
 */
public class MinimalRefineProjectLocation implements RefineProjectLocation {

   private final String id;
   private final URL url;

   private MinimalRefineProjectLocation(String id, URL url) {
      this.id = id;
      this.url = url;
   }

   /**
    * Factory method based on the location header value (URL).
    *
    * @param location the location header value
    * @return the instance
    * @throws MalformedURLException in case the location is not a valild URL
    * @since 0.1.3
    */
   public static MinimalRefineProjectLocation from(String location) throws MalformedURLException {
      notNull(location, "location");
      String id = substringAfterLast(location, "=");
      notEmpty(id, "id");
      return new MinimalRefineProjectLocation(id, new URL(location));
   }

   /**
    * Factory method based on an URL.
    *
    * @param url the URL
    * @return the instance
    */
   public static MinimalRefineProjectLocation from(URL url) {
      notNull(url, "url");
      String id = substringAfterLast(url.getQuery(), "=");
      notEmpty(id, "id");
      return new MinimalRefineProjectLocation(id, url);
   }

   @Override
   public String getId() {
      return id;
   }

   @Override
   public URL getUrl() {
      return url;
   }

   @Override
   public String toString() {
      return "MinimalRefineProjectLocation{" +
            "id='" + id + '\'' +
            ", url=" + url +
            '}';
   }
}
