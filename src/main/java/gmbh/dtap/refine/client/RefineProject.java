package gmbh.dtap.refine.client;

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * This interface defines a project representation of the <a href="https://github.com/OpenRefine/OpenRefine/wiki/OpenRefine-API"></a>OpenRefine API</a>.
 */
public interface RefineProject {

   /**
    * Returns the ID of the project.
    *
    * @return the project ID
    */
   String getId();

   /**
    * Returns the name of the project.
    *
    * @return the project name
    */
   String getName();

   /**
    * Return the URL of the project.
    *
    * @return the the project URL
    */
   URL getUrl();

   /**
    * Returns the project location, same as {@link RefineProject#getId()} and {@link RefineProject#getUrl()}.
    *
    * @return the project location
    */
   ProjectLocation getLocation();

   /**
    * Returns the creation date.
    *
    * @return the creation date
    */
   OffsetDateTime getCreated();

   /**
    * Returns the last modification date.
    *
    * @return the last modification date
    */
   OffsetDateTime getModified();

   /**
    * Returns the creator.
    *
    * @return the creator
    */
   String getCreator();

   /**
    * Returns the contributors.
    *
    * @return the contributors
    */
   String getContributors();

   /**
    * Returns the subject.
    *
    * @return the subject
    */
   String getSubject();

   /**
    * Returns the description.
    *
    * @return the description
    */
   String getDescription();

   /**
    * Returns the nuber of rows.
    *
    * @return the number of rows.
    */
   long getRowCount();

   /**
    * Returns custom metadata.
    *
    * @return the custom metadata
    */
   CustomMetadata getCustomMetadata();

   /**
    * Returns metadata from the import options.
    *
    * @return the import option metadata, never {@code null}
    */
   List<ImportOptionMetadata> getImportOptionMetadata();
}