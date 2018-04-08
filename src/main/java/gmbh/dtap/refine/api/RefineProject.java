package gmbh.dtap.refine.api;

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * This interface defines a project representation of the <a href="https://github.com/OpenRefine/OpenRefine/wiki/OpenRefine-API"></a>OpenRefine API</a>.
 *
 * @since 0.1.0
 */
public interface RefineProject {

   /**
    * Returns the ID of the project.
    *
    * @return the project ID
    * @since 0.1.0
    */
   String getId();

   /**
    * Returns the name of the project.
    *
    * @return the project name
    * @since 0.1.0
    */
   String getName();

   /**
    * Return the URL of the project.
    *
    * @return the the project URL
    * @since 0.1.0
    */
   URL getUrl();

   /**
    * Returns the project location, same as {@link RefineProject#getId()} and {@link RefineProject#getUrl()}.
    *
    * @return the project location
    * @since 0.1.2
    */
   RefineProjectLocation getLocation();

   /**
    * Returns the creation date.
    *
    * @return the creation date
    * @since 0.1.2
    */
   OffsetDateTime getCreated();

   /**
    * Returns the last modification date.
    *
    * @return the last modification date
    * @since 0.1.2
    */
   OffsetDateTime getModified();

   /**
    * Returns the creator.
    *
    * @return the creator
    * @since 0.1.2
    */
   String getCreator();

   /**
    * Returns the contributors.
    *
    * @return the contributors
    * @since 0.1.2
    */
   String getContributors();

   /**
    * Returns the subject.
    *
    * @return the subject
    * @since 0.1.2
    */
   String getSubject();

   /**
    * Returns the description.
    *
    * @return the description
    * @since 0.1.2
    */
   String getDescription();

   /**
    * Returns the nuber of rows.
    *
    * @return the number of rows.
    * @since 0.1.2
    */
   long getRowCount();

   /**
    * Returns custom metadata.
    *
    * @return the custom metadata
    * @since 0.1.2
    */
   CustomMetadata getCustomMetadata();

   /**
    * Returns metadata from the import options.
    *
    * @return the import option metadata, never {@code null}
    * @since 0.1.2
    */
   List<ImportOptionMetadata> getImportOptionMetadata();
}
