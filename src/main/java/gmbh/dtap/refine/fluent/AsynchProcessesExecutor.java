package gmbh.dtap.refine.fluent;

import gmbh.dtap.refine.api.*;

import java.io.IOException;
import java.util.List;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * A fluent executor for statuses of asynch processes.
 *
 * @see RefineExecutor#asynchProcesses()
 * @see RefineClient#asyncProcesses(String)
 * @since 0.1.8
 */
public class AsynchProcessesExecutor {

   private String projectId;

   /**
    * Sets the project ID.
    *
    * @param projectId the project ID
    * @return the executor for fluent usage
    * @since 0.1.8
    */
   public AsynchProcessesExecutor project(String projectId) {
      this.projectId = projectId;
      return this;
   }

   /**
    * Sets the project ID from the project location.
    *
    * @param projectLocation the project location
    * @return the executor for fluent usage
    * @since 0.1.8
    */
   public AsynchProcessesExecutor project(RefineProjectLocation projectLocation) {
      notNull(projectLocation, "projectLocation");
      this.projectId = projectLocation.getId();
      return this;
   }

   /**
    * Sets the project ID from the project.
    *
    * @param project the project
    * @return the executor for fluent usage
    * @since 0.1.8
    */
   public AsynchProcessesExecutor project(RefineProject project) {
      notNull(project, "project");
      this.projectId = project.getId();
      return this;
   }

   /**
    * Executes the request after validation.
    *
    * @param client the client to execute the request with
    * @return the list of process statuses
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the request failed
    * @since 0.1.8
    */
   public List<ProcessStatus> execute(RefineClient client) throws IOException {
      notNull(projectId, "projectId");
      notEmpty(projectId, "projectId is empty");
      return client.asyncProcesses(projectId);
   }
}
