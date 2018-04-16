package gmbh.dtap.refine.fluent;

import gmbh.dtap.refine.api.RefineClient;
import gmbh.dtap.refine.api.RefineException;
import gmbh.dtap.refine.api.RefineProject;
import gmbh.dtap.refine.api.RefineProjectLocation;

import java.io.IOException;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * A fluent request executor for the deletion of a project.
 *
 * @see RefineExecutor#deleteProject()
 * @see RefineClient#deleteProject(String)
 * @since 0.1.7
 */
public class DeleteProjectRequestExecutor {

   private String projectId;

   /**
    * Sets the project ID.
    *
    * @param projectId the project ID
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public DeleteProjectRequestExecutor project(String projectId) {
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
   public DeleteProjectRequestExecutor project(RefineProjectLocation projectLocation) {
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
   public DeleteProjectRequestExecutor project(RefineProject project) {
      notNull(project, "project");
      this.projectId = project.getId();
      return this;
   }

   /**
    * Executes the request after validation. The request is considered successful if no exception is thrown.
    *
    * @param client the client to execute the request with
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the request failed
    * @since 0.1.7
    */
   public void execute(RefineClient client) throws IOException {
      notNull(projectId, "projectId");
      notEmpty(projectId, "projectId is empty");
      client.deleteProject(projectId);
   }
}
