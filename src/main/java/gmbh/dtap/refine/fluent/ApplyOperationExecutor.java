package gmbh.dtap.refine.fluent;

import gmbh.dtap.refine.api.*;

import java.io.IOException;

import static org.apache.commons.lang3.Validate.*;

/**
 * A fluent request executor for applying an operation on a project.
 *
 * @see RefineExecutor#applyOperation()
 * @see RefineClient#applyOperations(String, Operation...)
 * @since 0.1.7
 */
public class ApplyOperationExecutor {

   private String projectId;
   private Operation[] operations;

   /**
    * Sets the project ID.
    *
    * @param projectId the project ID
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ApplyOperationExecutor project(String projectId) {
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
   public ApplyOperationExecutor project(RefineProjectLocation projectLocation) {
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
   public ApplyOperationExecutor project(RefineProject project) {
      notNull(project, "project");
      this.projectId = project.getId();
      return this;
   }

   /**
    * Sets one or more operations.
    *
    * @param operations the operations
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ApplyOperationExecutor operations(Operation... operations) {
      this.operations = operations;
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
      notNull(operations, "operations");
      notEmpty(operations, "operations is empty");
      noNullElements(operations, "operations contains null");
      client.applyOperations(projectId, operations);
   }
}
