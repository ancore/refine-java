package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.RefineProject;

import java.net.URL;
import java.util.Objects;

import static org.apache.http.util.Asserts.notEmpty;
import static org.apache.http.util.Asserts.notNull;

/**
 * This class implements a minimal {@link RefineProject}.
 *
 * @since 0.1.0
 */
public class MinimalRefineProject implements RefineProject {

   private final String id;
   private final String name;
   private final URL url;

   public MinimalRefineProject(String id, String name, URL url) {
      notEmpty(id, "id");
      notEmpty(name, "name");
      notNull(url, "url");
      this.id = id;
      this.name = name;
      this.url = url;
   }

   @Override
   public String getId() {
      return id;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public URL getUrl() {
      return url;
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, name, url);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      MinimalRefineProject that = (MinimalRefineProject) o;
      return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(url, that.url);
   }

   @Override
   public String toString() {
      return "MinimalRefineProject{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", url=" + url +
            '}';
   }
}
