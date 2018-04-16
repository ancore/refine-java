package gmbh.dtap.refine.fluent;

/**
 * Provides factory methods for request executors.
 *
 * @since 0.1.7
 */
public interface RefineExecutor {

   /**
    * Creates and returns a fluent executor for applying an operation on a project.
    *
    * @return the request executor
    * @since 0.1.7
    */
   static ApplyOperationExecutor applyOperation() {
      return new ApplyOperationExecutor();
   }

   /**
    * Creates and returns a fluent executor for statuses of asynchronous processes of a project.
    *
    * @return the request executor
    * @since 0.1.8
    */
   static AsynchProcessesExecutor asynchProcesses() {
      return new AsynchProcessesExecutor();
   }

   /**
    * Creates and returns a fluent executor for the creation of a project.
    *
    * @return the request executor
    * @since 0.1.7
    */
   static CreateProjectExecutor createProject() {
      return new CreateProjectExecutor();
   }

   /**
    * Creates and returns fluent executor for the deletion of a project.
    *
    * @return the request executor
    * @since 0.1.7
    */
   static DeleteProjectExecutor deleteProject() {
      return new DeleteProjectExecutor();
   }

   /**
    * Creates and returns a fluent executor to export (download) rows from a project.
    *
    * @return the request executor
    * @since 0.1.7
    */
   static ExportRowsExecutor exportRows() {
      return new ExportRowsExecutor();
   }

   /**
    * Creates and returns a fluent executor for expression previews.
    *
    * @return the request executor
    * @since 0.1.7
    */
   static ExpressionPreviewExecutor expressionPreview() {
      return new ExpressionPreviewExecutor();
   }

   /**
    * Creates and returns an executor to get the metadata of all projects.
    *
    * @return the request executor
    * @since 0.1.7
    */
   static ProjectMetadataExecutor projectMetadata() {
      return new ProjectMetadataExecutor();
   }
}
