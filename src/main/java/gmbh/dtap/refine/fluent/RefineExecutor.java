package gmbh.dtap.refine.fluent;

/**
 * Provides factory methods for request executors.
 *
 * @since 0.1.7
 */
public interface RefineExecutor {

   /**
    * Creates and returns a fluent request executor for applying an operation on a project.
    *
    * @return the request executor
    * @since 0.1.7
    */
   static ApplyOperationRequestExecutor applyOperation() {
      return new ApplyOperationRequestExecutor();
   }

   /**
    * Creates and returns a fluent request executor for the creation of a project.
    *
    * @return the request executor
    * @since 0.1.7
    */
   static CreateProjectRequestExecutor createProject() {
      return new CreateProjectRequestExecutor();
   }


   /**
    * Creates and returns fluent request executor for the deletion of a project.
    *
    * @return the request executor
    * @since 0.1.7
    */
   static DeleteProjectRequestExecutor deleteProject() {
      return new DeleteProjectRequestExecutor();
   }

   /**
    * Creates and returns a fluent request executor to export (download) rows from a project.
    *
    * @return the request executor
    * @since 0.1.7
    */
   static ExportRowsRequestExecutor exportRows() {
      return new ExportRowsRequestExecutor();
   }

   /**
    * Creates and returns a fluent  executor for the <tt>expression preview</tt> request.
    *
    * @return the request executor
    * @since 0.1.7
    */
   static ExpressionPreviewRequestExecutor expressionPreview() {
      return new ExpressionPreviewRequestExecutor();
   }

   /**
    * Creates and returns a request executor to get the metadata of all projects
    *
    * @return the request executor
    * @since 0.1.7
    */
   static ProjectMetadataRequestExecutor projectMetadata() {
      return new ProjectMetadataRequestExecutor();
   }
}
