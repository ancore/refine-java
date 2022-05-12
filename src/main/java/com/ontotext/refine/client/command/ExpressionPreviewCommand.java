package com.ontotext.refine.client.command;

import static com.ontotext.refine.client.util.HttpParser.HTTP_PARSER;
import static com.ontotext.refine.client.util.JsonParser.JSON_PARSER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.JsonNode;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * A command to preview expressions on a project.
 */
public class ExpressionPreviewCommand implements RefineCommand<ExpressionPreviewResponse> {

  private final String projectId;
  private final Integer cellIndex;
  private final long[] rowIndices;
  private final String expression;
  private final boolean repeat;
  private final int repeatCount;
  private final String token;

  private ExpressionPreviewCommand(
      String projectId,
      Integer cellIndex,
      long[] rowIndices,
      String expression,
      boolean repeat,
      int repeatCount,
      String token) {
    this.projectId = projectId;
    this.cellIndex = cellIndex;
    this.rowIndices = rowIndices;
    this.expression = expression;
    this.repeat = repeat;
    this.repeatCount = repeatCount;
    this.token = token;
  }

  @Override
  public String endpoint() {
    return "/orefine/command/core/preview-expression";
  }

  @Override
  public ExpressionPreviewResponse execute(RefineClient client) throws RefineException {
    try {
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

      HttpUriRequest request = RequestBuilder
          .post(client.createUri(endpoint()))
          .addParameter(Constants.CSRF_TOKEN, token)
          .setHeader(ACCEPT, APPLICATION_JSON.getMimeType())
          .setEntity(new UrlEncodedFormEntity(form,
              UTF_8))
          .build();

      return client.execute(request, this);
    } catch (IOException ioe) {
      throw new RefineException(
          "Failed to get expression preview for project: '%s' due to: %s",
          projectId,
          ioe.getMessage());
    }
  }

  @Override
  public ExpressionPreviewResponse handleResponse(HttpResponse response) throws IOException {
    HTTP_PARSER.assureStatusCode(response, SC_OK);
    String responseBody = EntityUtils.toString(response.getEntity(), UTF_8);
    return parseExpressionPreviewResponse(responseBody);
  }

  ExpressionPreviewResponse parseExpressionPreviewResponse(String json) throws IOException {
    JsonNode node = JSON_PARSER.parseJson(json);
    String code = JSON_PARSER.findExistingPath(node, "code").asText();
    if ("ok".equals(code)) {
      JsonNode resultsNode = JSON_PARSER.findExistingPath(node, "results");
      if (resultsNode.isMissingNode()) {
        return ExpressionPreviewResponse.ok(Collections.emptyList());
      }

      if (resultsNode.isArray()) {
        List<String> results = JSON_PARSER.toResults(resultsNode);
        return ExpressionPreviewResponse.ok(results);
      }

      throw new RefineException("Node with path 'results' is not any array: %s", resultsNode);
    }

    if ("error".equals(code)) {
      String message = JSON_PARSER.findExistingPath(node, "message").asText();
      return ExpressionPreviewResponse.error(message);
    }

    throw new RefineException("Unexpected code: %s", code);
  }

  /**
   * The builder for {@link ExpressionPreviewCommand}.
   */
  public static class Builder {

    private String projectId;
    private int cellIndex;
    private long[] rowIndices;
    private String expression;
    private boolean repeat;
    private int repeatCount;
    private String token;

    /**
     * Sets the project ID.
     *
     * @param projectId the project ID
     * @return the builder for fluent usage
     */
    public Builder project(String projectId) {
      this.projectId = projectId;
      return this;
    }

    /**
     * Sets the project ID.
     *
     * @param token the csrf token
     * @return the builder for fluent usage
     */
    public Builder token(String token) {
      this.token = token;
      return this;
    }

    /**
     * Sets the cell/column to execute the expression on.
     *
     * @param cellIndex the cell/column to execute the expression on
     * @return the builder for fluent usage
     */
    public Builder cellIndex(int cellIndex) {
      this.cellIndex = cellIndex;
      return this;
    }

    /**
     * Sets the rows to execute the expression on.
     *
     * @param rowIndices the rows to execute the expression on
     * @return the builder for fluent usage
     */
    public Builder rowIndices(long... rowIndices) {
      this.rowIndices = rowIndices;
      return this;
    }

    /**
     * Sets the rows to execute the expression on.
     *
     * @param expression the expression to execute, prefix for the language is expected, e.g.:
     *        "grel:toLowercase(value)"
     * @return the builder for fluent usage
     */
    public Builder expression(String expression) {
      this.expression = expression;
      return this;
    }

    /**
     * Sets the <code>grel</code> expression to execute.
     *
     * @param expression the expression in <code>grel</code> to execute
     * @return the builder for fluent usage
     */
    public Builder grel(String expression) {
      this.expression = "grel:" + expression;
      return this;
    }

    /**
     * Sets the <code>jython</code> expression to execute.
     *
     * @param expression the expression <code>jython</code> to execute
     * @return the builder for fluent usage
     */
    public Builder jython(String expression) {
      this.expression = "jython:" + expression;
      return this;
    }

    /**
     * Sets the <code>clojure</code> expression to execute.
     *
     * @param expression the expression <code>clojure</code> to execute
     * @return the builder for fluent usage
     */
    public Builder clojure(String expression) {
      this.expression = "clojure:" + expression;
      return this;
    }

    /**
     * Sets whether or not to repeated the command multiple times.
     *
     * @param repeat whether or not to repeated the expression multiple times
     * @return the builder for fluent usage
     */
    public Builder repeat(boolean repeat) {
      this.repeat = repeat;
      return this;
    }

    /**
     * Sets the maximum amount of times a command will be repeated.
     *
     * @param repeatCount the maximum amount of times a command will be repeated
     * @return the builder for fluent usage
     */
    public Builder repeatCount(int repeatCount) {
      this.repeatCount = repeatCount;
      return this;
    }

    /**
     * Builds the command after validation.
     *
     * @return the command
     */
    public ExpressionPreviewCommand build() {
      notBlank(projectId, "projectId is blank");
      notNull(rowIndices, "rowIndices");
      notNull(expression, "expression");
      notNull(token, "token");
      return new ExpressionPreviewCommand(
          projectId, cellIndex, rowIndices, expression, repeat, repeatCount, token);
    }
  }
}
