package gmbh.dtap.refine.client;

import java.net.URL;

import static org.apache.commons.lang.StringUtils.substringAfterLast;

public class CreateProjectResponse {

   private final URL location;

   CreateProjectResponse(URL location) {
      this.location = location;
   }

   public URL getLocation() {
      return location;
   }

   public String getProjectId() {
      return substringAfterLast(location.getQuery(), "=");
   }

   @Override
   public String toString() {
      final StringBuffer sb = new StringBuffer("CreateProjectResponse{");
      sb.append("location=").append(location);
      sb.append('}');
      return sb.toString();
   }
}
