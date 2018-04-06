package gmbh.dtap.refine.api;

import java.net.URL;

/**
 * This interface defines a project representation of the <a href="https://github.com/OpenRefine/OpenRefine/wiki/OpenRefine-API"></a>OpenRefine API</a>.
 *
 * @since 0.1.0
 */
public interface RefineProject {

   /**
    * Returns the ID of the project as returned by OpenRefine server on creation.
    *
    * @return the project ID
    * @since 0.1.0
    */
   String getId();

   /**
    * Returns the name of the project as requested on creation.
    *
    * @return the project name
    * @since 0.1.0
    */
   String getName();

   /**
    * Return the URL of the project as returned by OpenRefine server on creation.
    *
    * @return the the project URL
    * @since 0.1.0
    */
   URL getUrl();
}
