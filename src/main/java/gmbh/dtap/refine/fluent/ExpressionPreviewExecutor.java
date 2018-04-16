package gmbh.dtap.refine.fluent;

import gmbh.dtap.refine.api.RefineClient;
import gmbh.dtap.refine.api.RefineException;
import gmbh.dtap.refine.api.RefineProject;
import gmbh.dtap.refine.api.RefineProjectLocation;

import java.io.IOException;
import java.util.List;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * A fluent executor for expression previews.
 *
 * @see RefineExecutor#expressionPreview()
 * @see RefineClient#expressionPreview(String, long, long[], String, boolean, int)
 * @since 0.1.7
 */
public class ExpressionPreviewExecutor {

   private String projectId;
   private Integer cellIndex;
   private long[] rowIndices;
   private String expression;
   private boolean repeat;
   private int repeatCount;

   /**
    * Sets the project ID.
    *
    * @param projectId the project ID
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ExpressionPreviewExecutor project(String projectId) {
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
   public ExpressionPreviewExecutor project(RefineProjectLocation projectLocation) {
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
   public ExpressionPreviewExecutor project(RefineProject project) {
      notNull(project, "project");
      this.projectId = project.getId();
      return this;
   }

   /**
    * Sets the cell/column to execute the expression on.
    *
    * @param cellIndex the cell/column to execute the expression on
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ExpressionPreviewExecutor cellIndex(int cellIndex) {
      this.cellIndex = cellIndex;
      return this;
   }

   /**
    * Sets the rows to execute the expression on.
    *
    * @param rowIndices the rows to execute the expression on
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ExpressionPreviewExecutor rowIndices(long... rowIndices) {
      this.rowIndices = rowIndices;
      return this;
   }

   /**
    * Sets the rows to execute the expression on.
    *
    * @param expression the expression to execute, prefix for the language is expected,
    *                   e.g.: "grel:toLowercase(value)"
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ExpressionPreviewExecutor expression(String expression) {
      this.expression = expression;
      return this;
   }

   /**
    * Sets the <tt>grel</tt> expression to execute.
    *
    * @param expression the expression in <tt>grel</tt> to execute
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ExpressionPreviewExecutor grel(String expression) {
      this.expression = "grel:" + expression;
      return this;
   }

   /**
    * Sets the <tt>jython</tt> expression to execute.
    *
    * @param expression the expression <tt>jython</tt> to execute
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ExpressionPreviewExecutor jython(String expression) {
      this.expression = "jython:" + expression;
      return this;
   }

   /**
    * Sets the <tt>clojure</tt> expression to execute.
    *
    * @param expression the expression <tt>clojure</tt> to execute
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ExpressionPreviewExecutor clojure(String expression) {
      this.expression = "clojure:" + expression;
      return this;
   }

   /**
    * Sets whether or not to repeated the command multiple times.
    *
    * @param repeat whether or not to repeated the expression multiple times
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ExpressionPreviewExecutor repeat(boolean repeat) {
      this.repeat = repeat;
      return this;
   }

   /**
    * Sets the maximum amount of times a command will be repeated.
    *
    * @param repeatCount the maximum amount of times a command will be repeated
    * @return the executor for fluent usage
    * @since 0.1.7
    */
   public ExpressionPreviewExecutor repeatCount(int repeatCount) {
      this.repeatCount = repeatCount;
      return this;
   }

   /**
    * Executes the request after validation.
    *
    * @param client the client to execute the request with
    * @return the list of expression previews
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the request failed
    * @since 0.1.7
    */
   public List<String> execute(RefineClient client) throws IOException {
      notNull(projectId, "projectId");
      notEmpty(projectId, "projectId is empty");
      notNull(rowIndices, "rowIndices");
      notNull(expression, "expression");
      return client.expressionPreview(projectId, cellIndex, rowIndices, expression, repeat, repeatCount);
   }
}
