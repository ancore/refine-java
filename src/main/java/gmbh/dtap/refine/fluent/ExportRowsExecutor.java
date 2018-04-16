package gmbh.dtap.refine.fluent;

import gmbh.dtap.refine.api.*;

import java.io.IOException;
import java.io.OutputStream;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * A fluent request executor to export (download) rows from a project.
 *
 * @see RefineExecutor#exportRows()
 * @see RefineClient#exportRows(String, Engine, ExportFormat, OutputStream)
 * @since 0.1.7
 */
public class ExportRowsExecutor {

   private String projectId;
   private Engine engine;
   private ExportFormat format;
   private OutputStream outputStream;

   /**
    * Sets the project ID.
    *
    * @param projectId the project ID
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ExportRowsExecutor project(String projectId) {
      this.projectId = projectId;
      return this;
   }

   /**
    * Sets the project ID from the project location.
    *
    * @param projectLocation the project location
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ExportRowsExecutor project(RefineProjectLocation projectLocation) {
      notNull(projectLocation, "projectLocation");
      this.projectId = projectLocation.getId();
      return this;
   }

   /**
    * Sets the project ID from the project.
    *
    * @param project the project
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ExportRowsExecutor project(RefineProject project) {
      notNull(project, "project");
      this.projectId = project.getId();
      return this;
   }

   /**
    * Sets the optional restrictions.
    *
    * @param engine the optional restrictions
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ExportRowsExecutor engine(Engine engine) {
      this.engine = engine;
      return this;
   }

   /**
    * Sets the export format.
    *
    * @param format the export format
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ExportRowsExecutor format(ExportFormat format) {
      this.format = format;
      return this;
   }


   /**
    * Sets the output stream to export to.
    *
    * @param outputStream the output stream
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ExportRowsExecutor outputStream(OutputStream outputStream) {
      this.outputStream = outputStream;
      return this;
   }

   /**
    * Executes the request after validation. The request is considered successful if no exception is thrown.
    *
    * @param client the client to execute the request with
    * @return the number of bytes written
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the request failed
    * @see RefineClient#exportRows(String, Engine, ExportFormat, OutputStream)
    * @since 0.1.7
    */
   public int execute(RefineClient client) throws IOException {
      notNull(projectId, "projectId");
      notEmpty(projectId, "projectId is empty");
      notNull(format, "format");
      notNull(outputStream, "outputStream");
      return client.exportRows(projectId, engine, format, outputStream);
   }
}

