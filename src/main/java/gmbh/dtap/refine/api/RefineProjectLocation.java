package gmbh.dtap.refine.api;

import java.net.URL;

/**
 * Defines the project URL. THe ID is part of the URL and provided additionally by its onw getter.
 * <p>
 * When creating a project, the server only responds with the location (URL) of the new project.
 * More project meta data can be acquired with {@link RefineClient#getAllProjectMetadata()}.
 *
 * @since 0.1.2
 */
public interface RefineProjectLocation {

   /**
    * Returns the ID of the project. THe ID is present as query parameter <tt>project</tt> in the URL also.
    *
    * @return the project ID
    * @since 0.1.2
    */
   String getId();

   /**
    * Return the URL of the project.
    *
    * @return the the project URL
    * @since 0.1.2
    */
   URL getUrl();
}
