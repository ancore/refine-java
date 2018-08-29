package gmbh.dtap.refine.client;

/**
 * Upload options can be provided when creating a {@link RefineProject}.
 * Options are used to give additional information on an {@link UploadFormat}.
 */
public interface UploadOptions {

   /**
    * Returns the options as JSON_PARSER in the format expected by OpenRefine.
    *
    * @return the options as JSON_PARSER
    */
   String asJson();
}
