package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.RefineProjectLocation;

import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.lang.StringUtils.substringAfterLast;
import static org.apache.http.util.Asserts.notEmpty;
import static org.apache.http.util.Asserts.notNull;

public class MinimalRefineProjectLocation implements RefineProjectLocation {

   private final String id;
   private final URL url;

   private MinimalRefineProjectLocation(String id, URL url) {
      this.id = id;
      this.url = url;
   }

   public static MinimalRefineProjectLocation from(String location) throws MalformedURLException {
      notNull(location, "location");
      String id = substringAfterLast(location, "=");
      notEmpty(id, "id");
      return new MinimalRefineProjectLocation(id, new URL(location));
   }

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
