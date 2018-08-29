package gmbh.dtap.refine.client;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

/**
 * A command to preview expressions on a project.
 */
public class ExpressionPreviewCommand {

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
    * @return the command for fluent usage
    */
   public ExpressionPreviewCommand project(String projectId) {
      this.projectId = projectId;
      return this;
   }

   /**
    * Sets the project ID from the project location.
    *
    * @param projectLocation the project location
    * @return the command for fluent usage
    */
   public ExpressionPreviewCommand project(ProjectLocation projectLocation) {
      notNull(projectLocation, "projectLocation");
      this.projectId = projectLocation.getId();
      return this;
   }

   /**
    * Sets the project ID from the project.
    *
    * @param project the project
    * @return the command for fluent usage
    */
   public ExpressionPreviewCommand project(RefineProject project) {
      notNull(project, "project");
      this.projectId = project.getId();
      return this;
   }

   /**
    * Sets the cell/column to execute the expression on.
    *
    * @param cellIndex the cell/column to execute the expression on
    * @return the command for fluent usage
    */
   public ExpressionPreviewCommand cellIndex(int cellIndex) {
      this.cellIndex = cellIndex;
      return this;
   }

   /**
    * Sets the rows to execute the expression on.
    *
    * @param rowIndices the rows to execute the expression on
    * @return the command for fluent usage
    */
   public ExpressionPreviewCommand rowIndices(long... rowIndices) {
      this.rowIndices = rowIndices;
      return this;
   }

   /**
    * Sets the rows to execute the expression on.
    *
    * @param expression the expression to execute, prefix for the language is expected,
    *                   e.g.: "grel:toLowercase(value)"
    * @return the command for fluent usage
    */
   public ExpressionPreviewCommand expression(String expression) {
      this.expression = expression;
      return this;
   }

   /**
    * Sets the <tt>grel</tt> expression to execute.
    *
    * @param expression the expression in <tt>grel</tt> to execute
    * @return the command for fluent usage
    */
   public ExpressionPreviewCommand grel(String expression) {
      this.expression = "grel:" + expression;
      return this;
   }

   /**
    * Sets the <tt>jython</tt> expression to execute.
    *
    * @param expression the expression <tt>jython</tt> to execute
    * @return the command for fluent usage
    */
   public ExpressionPreviewCommand jython(String expression) {
      this.expression = "jython:" + expression;
      return this;
   }

   /**
    * Sets the <tt>clojure</tt> expression to execute.
    *
    * @param expression the expression <tt>clojure</tt> to execute
    * @return the command for fluent usage
    */
   public ExpressionPreviewCommand clojure(String expression) {
      this.expression = "clojure:" + expression;
      return this;
   }

   /**
    * Sets whether or not to repeated the command multiple times.
    *
    * @param repeat whether or not to repeated the expression multiple times
    * @return the command for fluent usage
    */
   public ExpressionPreviewCommand repeat(boolean repeat) {
      this.repeat = repeat;
      return this;
   }

   /**
    * Sets the maximum amount of times a command will be repeated.
    *
    * @param repeatCount the maximum amount of times a command will be repeated
    * @return the command for fluent usage
    */
   public ExpressionPreviewCommand repeatCount(int repeatCount) {
      this.repeatCount = repeatCount;
      return this;
   }

   /**
    * Executes the request after validation.
    *
    * @param client the client to execute the request with
    * @return the result of the command
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the request failed
    */
   public ExpressionPreviewResponse execute(RefineClient client) throws IOException {
      notNull(projectId, "projectId");
      notEmpty(projectId, "projectId is empty");
      notNull(rowIndices, "rowIndices");
      notNull(expression, "expression");

      URL url = client.createUrl("/command/core/preview-expression");

      StringJoiner joiner = new StringJoiner(",");
      for (long rowIndex : rowIndices) {
         joiner.add(String.valueOf(rowIndex));
      }
      String rowIndicesJson = "[" + joiner + "]";

      List<NameValuePair> form = new ArrayList<>();
      form.add(new BasicNameValuePair("cellIndex", String.valueOf(cellIndex)));
      form.add(new BasicNameValuePair("rowIndices", rowIndicesJson));
      form.add(new BasicNameValuePair("expression", expression));
      form.add(new BasicNameValuePair("project", projectId));
      form.add(new BasicNameValuePair("repeat", String.valueOf(repeat)));
      form.add(new BasicNameValuePair("repeatCount", String.valueOf(repeatCount)));

      UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

      HttpUriRequest request = RequestBuilder
            .post(url.toString())
            .setHeader(ACCEPT, APPLICATION_JSON.getMimeType())
            .setEntity(entity)
            .build();

      return client.execute(request, new ExpressionPreviewResponseHandler());
   }
}
