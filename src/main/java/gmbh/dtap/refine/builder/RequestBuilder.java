package gmbh.dtap.refine.builder;

/**
 * Provides factory methods for request builders.
 *
 * @since 0.1.7
 */
public interface RequestBuilder {

   /**
    * Creates and returns a builder for the <tt>apply operation</tt> request.
    *
    * @return the request builder
    * @since 0.1.7
    */
   static ApplyOperationRequest applyOperation() {
      return new ApplyOperationRequest();
   }

   /**
    * Creates and returns a builder for the <tt>asynchronous processes status</tt> request.
    *
    * @return the request builder
    * @since 0.1.7
    */
   static AsynchProcessesStatus asynchProcessesStatus() {
      return new AsynchProcessesStatus();
   }

   /**
    * Creates and returns a builder for the <tt>crate project</tt> request.
    *
    * @return the request builder
    * @since 0.1.7
    */
   static CreateProjectRequest createProject() {
      return new CreateProjectRequest();
   }

   /**
    * Creates and returns a builder for the <tt>export rows</tt> request.
    *
    * @return the request builder
    * @since 0.1.7
    */
   static ExportRowsRequest exportRows() {
      return new ExportRowsRequest();
   }

   /**
    * Creates and returns a builder for the <tt>expression preview</tt> request.
    *
    * @return the request builder
    * @since 0.1.7
    */
   static ExpressionPreviewRequest expressionPreview() {
      return new ExpressionPreviewRequest();
   }

   /**
    * Creates and returns a builder for the <tt>get all project metadata</tt> request.
    *
    * @return the request builder
    * @since 0.1.7
    */
   static ProjectMetadataRequest projectMetadata() {
      return new ProjectMetadataRequest();
   }
}
