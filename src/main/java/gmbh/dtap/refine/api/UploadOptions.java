package gmbh.dtap.refine.api;

/**
 * Upload options can be provided when creating a {@link RefineProject}.
 * Options are used to give additional information on an {@link UploadFormat}.
 *
 * @since 0.1.0
 */
public interface UploadOptions {

   /**
    * Returns the options as JSON in the format expected by OpenRefine.
    *
    * @return the options as JSON
    * @since 0.1.0
    */
   String asJson();
}
