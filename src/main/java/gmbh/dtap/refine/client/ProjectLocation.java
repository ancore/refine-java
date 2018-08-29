package gmbh.dtap.refine.client;

import java.net.URL;

/**
 * Defines the project URL. THe ID is part of the URL and provided additionally by its onw getter.
 * <p>
 * When creating a project, the server only responds with the location (URL) of the new project.
 s */
public interface ProjectLocation {

   /**
    * Returns the ID of the project. The ID is present as query parameter <tt>project</tt> in the URL also.
    *
    * @return the project ID
    */
   String getId();

   /**
    * Return the URL of the project.
    *
    * @return the the project URL
    */
   URL getUrl();
}
