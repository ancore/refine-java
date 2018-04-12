package gmbh.dtap.refine.builder;

import gmbh.dtap.refine.api.RefineClient;
import gmbh.dtap.refine.api.RefineException;
import gmbh.dtap.refine.api.RefineProjectLocation;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * A builder for the <tt>crate project</tt> request.
 *
 * @since 0.1.7
 */
class CreateProjectRequest implements RequestBuilder {

   private String name;
   private File file;

   /**
    * @see RequestBuilder
    * @since 0.1.7
    */
   CreateProjectRequest() {
   }

   /**
    * Sets the project name.
    *
    * @param name the project name
    * @return the request for fluent usage
    * @since 0.1.7
    */
   public CreateProjectRequest name(String name) {
      this.name = name;
      return this;
   }

   /**
    * Sets the file to upload.
    *
    * @param file the file to upload
    * @return the request for fluent usage
    * @since 0.1.7
    */
   public CreateProjectRequest file(File file) {
      this.file = file;
      return this;
   }

   /**
    * Validates and executes the request.
    *
    * @param refineClient the client to execute the request with
    * @return the result of the request
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the request failed
    * @since 0.1.7
    */
   public RefineProjectLocation execute(RefineClient refineClient) throws IOException {
      notNull(refineClient, "refineClient");
      notEmpty(name, "name");
      notNull(file, "file");
      return refineClient.createProject(name, file);
   }
}
