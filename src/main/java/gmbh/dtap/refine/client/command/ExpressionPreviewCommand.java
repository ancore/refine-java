/*
 * Copyright 2019 DTAP GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gmbh.dtap.refine.client.command;

import com.fasterxml.jackson.databind.JsonNode;
import gmbh.dtap.refine.client.ProjectLocation;
import gmbh.dtap.refine.client.RefineClient;
import gmbh.dtap.refine.client.RefineException;
import gmbh.dtap.refine.client.RefineProject;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import static gmbh.dtap.refine.client.util.HttpParser.HTTP_PARSER;
import static gmbh.dtap.refine.client.util.JsonParser.JSON_PARSER;
import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

/**
 * A command to preview expressions on a project.
 */
public class ExpressionPreviewCommand implements ResponseHandler<ExpressionPreviewResponse> {

   private final String projectId;
   private final Integer cellIndex;
   private final long[] rowIndices;
   private final String expression;
   private final boolean repeat;
   private final int repeatCount;
   private final String token;
   private final String CSRF_TOKEN = "csrf_token=";

   /**
	* Constructor for {@link Builder}.
	*
	* @param projectId   the project ID
	* @param cellIndex   the cell/column to execute the expression on
	* @param rowIndices  the rows to execute the expression on
	* @param expression  the expression to execute
	* @param repeat      whether or not to repeated the expression multiple times
	* @param repeatCount the maximum amount of times a command will be repeated
	* @param token       the csrf token
	*/
   public ExpressionPreviewCommand(String projectId, Integer cellIndex, long[] rowIndices, String expression,
								   boolean repeat, int repeatCount, String token) {
	  this.projectId = projectId;
	  this.cellIndex = cellIndex;
	  this.rowIndices = rowIndices;
	  this.expression = expression;
	  this.repeat = repeat;
	  this.repeatCount = repeatCount;
	  this.token = token;
   }

   /**
	* Executes the request.
	*
	* @param client the client to execute the request with
	* @return the result of the command
	* @throws IOException     in case of a connection problem
	* @throws RefineException in case the request failed
	*/
   public ExpressionPreviewResponse execute(RefineClient client) throws IOException {
	  URL url = client.createUrl("/command/core/preview-expression?" + CSRF_TOKEN + token);

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

	  HttpUriRequest request = RequestBuilder.post(url.toString()).setHeader(ACCEPT, APPLICATION_JSON.getMimeType())
			.setEntity(entity).build();

	  return client.execute(request, this);
   }

   /**
	* Validates the response and extracts necessary data.
	*
	* @param response the response to extract data from
	* @return the response representation
	* @throws IOException     in case of a connection problem
	* @throws RefineException in case the server responses with an unexpected
	*                         status or is not understood
	*/
   @Override
   public ExpressionPreviewResponse handleResponse(HttpResponse response) throws IOException {
	  HTTP_PARSER.assureStatusCode(response, SC_OK);
	  String responseBody = EntityUtils.toString(response.getEntity());
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
		 } else {
			throw new RefineException("Node with path 'results' is not any array: " + resultsNode);
		 }
	  } else if ("error".equals(code)) {
		 String message = JSON_PARSER.findExistingPath(node, "message").asText();
		 return ExpressionPreviewResponse.error(message);
	  } else {
		 throw new RefineException("Unexpected code: " + code);
	  }
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
	   * Sets the project ID from the project location.
	   *
	   * @param projectLocation the project location
	   * @return the builder for fluent usage
	   */
	  public Builder project(ProjectLocation projectLocation) {
		 notNull(projectLocation, "projectLocation");
		 this.projectId = projectLocation.getId();
		 return this;
	  }

	  /**
	   * Sets the project ID from the project.
	   *
	   * @param project the project
	   * @return the builder for fluent usage
	   */
	  public Builder project(RefineProject project) {
		 notNull(project, "project");
		 this.projectId = project.getId();
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
	   * @param expression the expression to execute, prefix for the language is
	   *                   expected, e.g.: "grel:toLowercase(value)"
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
		 notNull(projectId, "projectId");
		 notEmpty(projectId, "projectId is empty");
		 notNull(rowIndices, "rowIndices");
		 notNull(expression, "expression");
		 notNull(token, "token");
		 return new ExpressionPreviewCommand(projectId, cellIndex, rowIndices, expression, repeat, repeatCount,
			   token);
	  }
   }
}
