package gmbh.dtap.refine.fluent;

import gmbh.dtap.refine.api.*;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * A fluent request executor for the creation of a project.
 *
 * @see RefineExecutor#createProject()
 * @see RefineClient#createProject(String, File)
 * @see RefineClient#createProject(String, File, UploadFormat, UploadOptions)
 * @since 0.1.7
 */
public class CreateProjectExecutor {

   private String name;
   private File file;
   private UploadFormat format;
   private UploadOptions options;

   /**
    * Sets the project name.
    *
    * @param name the project name
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public CreateProjectExecutor name(String name) {
      this.name = name;
      return this;
   }

   /**
    * Sets the file containing the data to upload.
    *
    * @param file the file containing the data to upload
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public CreateProjectExecutor file(File file) {
      this.file = file;
      return this;
   }

   /**
    * Sets the optional upload format.
    *
    * @param format the optional upload format
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public CreateProjectExecutor format(UploadFormat format) {
      this.format = format;
      return this;
   }

   /**
    * Sets the optional options.
    *
    * @param options the optional options
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public CreateProjectExecutor options(UploadOptions options) {
      this.options = options;
      return this;
   }

   /**
    * Executes the request after validation.
    *
    * @param client the client to execute the request with
    * @return the location of the new project
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the request failed
    * @since 0.1.7
    */
   public RefineProjectLocation execute(RefineClient client) throws IOException {
      notNull(name, "name");
      notEmpty(name, "name");
      notNull(file, "file");
      return client.createProject(name, file, format, options);
   }
}
