package gmbh.dtap.refine.client;

public interface RefineCommands {

   static ApplyOperationsCommand applyOperations() {
      return new ApplyOperationsCommand();
   }

   static CreateProjectCommand createProject() {
      return new CreateProjectCommand();
   }

   static DeleteProjectCommand deleteProject() {
      return new DeleteProjectCommand();
   }

   static ExpressionPreviewCommand expressionPreview() {
      return new ExpressionPreviewCommand();
   }

   static GetVersionCommand getVersion() {
      return new GetVersionCommand();
   }

}
