package gmbh.dtap.refine.builder;

import gmbh.dtap.refine.api.RefineClient;
import gmbh.dtap.refine.api.RefineException;
import gmbh.dtap.refine.api.RefineProjectLocation;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

class ExpressionPreviewRequest {

   private static final String EXPRESSION_PATTEN = "(grel|jython|clojure)\\:.*";

   private String projectId;
   private Integer cellIndex;
   private long[] rowIndices;
   private String expression;
   private boolean repeat;
   private int repeatCount;

   /**
    * @see RequestBuilder
    */
   ExpressionPreviewRequest() {
   }

   /**
    * Sets the project ID from the location.
    *
    * @param location the location of the project
    * @return the request for fluent usage
    * @see #forProject(String) as alternative
    * @since 0.1.7
    */
   public ExpressionPreviewRequest forProject(RefineProjectLocation location) {
      notNull(location, "location");
      return forProject(location.getId());
   }

   /**
    * Sets the project ID.
    *
    * @param projectId the project ID
    * @return the request for fluent usage
    * @see #forProject(RefineProjectLocation) as alternative
    * @since 0.1.7
    */
   public ExpressionPreviewRequest forProject(String projectId) {
      notNull(projectId, "projectId");
      this.projectId = projectId;
      return this;
   }

   /**
    * Sets the cell/column to execute the expression on.
    *
    * @param cellIndex the cell/column to execute the expression on
    * @return the request for fluent usage
    * @since 0.1.7
    */
   public ExpressionPreviewRequest cellIndex(int cellIndex) {
      this.cellIndex = cellIndex;
      return this;
   }

   /**
    * Sets the rows to execute the expression on.
    *
    * @param rowIndices the rows to execute the expression on
    * @return the request for fluent usage
    * @since 0.1.7
    */
   public ExpressionPreviewRequest rowIndices(long... rowIndices) {
      notNull(rowIndices, "rowIndices");
      this.rowIndices = rowIndices;
      return this;
   }

   /**
    * Sets an expression in language <tt>grel</tt>.
    *
    * @param expression the expression, no prefix required
    * @return the request for fluent usage
    * @see #jython(String)
    * @see #clojure(String)
    * @see #expression(String)
    * @since 0.1.7
    */
   public ExpressionPreviewRequest grel(String expression) {
      return expression("grel:" + expression);
   }

   /**
    * Sets an expression in language <tt>jython</tt>.
    *
    * @param expression the expression, no prefix required
    * @return the request for fluent usage
    * @see #grel(String)
    * @see #clojure(String)
    * @see #expression(String)
    * @since 0.1.7
    */
   public ExpressionPreviewRequest jython(String expression) {
      return expression("jython:" + expression);
   }

   /**
    * Sets an expression in language <tt>clojure</tt>.
    *
    * @param expression the expression, no prefix required
    * @return the request for fluent usage
    * @see #grel(String)
    * @see #jython(String)
    * @see #expression(String)
    * @since 0.1.7
    */
   public ExpressionPreviewRequest clojure(String expression) {
      return expression("clojure:" + expression);
   }

   /**
    * Sets an expression prefixed with its language, e.g. "grel:toLowercase(value)".
    *
    * @param expression the expression with language prefix
    * @return the request for fluent usage
    * @see #grel(String)
    * @see #jython(String)
    * @see #clojure(String)
    * @since 0.1.7
    */
   public ExpressionPreviewRequest expression(String expression) {
      this.expression = requireNonNull(expression, "expression");
      return this;
   }

   /**
    * Sets whether or not this command should be repeated multiple times.
    * A repeated command will be executed until the result of the current
    * iteration equals the result of the previous iteration.
    *
    * @param repeat indicating whether or not this command should be repeated multiple times
    * @return the request for fluent usage
    * @see #repeatCount(int) to limit repetition
    * @since 0.1.7
    */
   public ExpressionPreviewRequest repeat(boolean repeat) {
      this.repeat = repeat;
      return this;
   }

   /**
    * Sets the maximum amount of times a command will be repeated.
    *
    * @param repeatCount the maximum amount of times a command will be repeated
    * @return the request for fluent usage
    * @see #repeat(boolean) for activation
    * @since 0.1.7
    */
   public ExpressionPreviewRequest repeatCount(int repeatCount) {
      this.repeatCount = repeatCount;
      return this;
   }

   /**
    * Validates and excutes the request.
    *
    * @param refineClient the client to execute the request with
    * @return the result of the request
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the request failed
    * @since 0.1.7
    */
   public List<String> execute(RefineClient refineClient) throws IOException {
      notNull(refineClient, "refineClient");
      notNull(projectId, "projectId");
      notNull(cellIndex, "cellIndex");
      notNull(rowIndices, "rowIndices");
      notNull(expression, "expression");
      isTrue(expression.matches(EXPRESSION_PATTEN), "expression does not match " + EXPRESSION_PATTEN);
      return refineClient.expressionPreview(projectId, cellIndex, rowIndices, expression, repeat, repeatCount);
   }
}
