/*
 * MIT License
 *
 * Copyright (c) 2018-2020 DTAP GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gmbh.dtap.refine.client.command;

import static gmbh.dtap.refine.client.util.HttpParser.HTTP_PARSER;
import static gmbh.dtap.refine.client.util.JsonParser.JSON_PARSER;
import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;

import gmbh.dtap.refine.client.ProjectLocation;
import gmbh.dtap.refine.client.RefineClient;
import gmbh.dtap.refine.client.RefineException;
import gmbh.dtap.refine.client.RefineProject;

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
		 * @param token
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
		 * Sets the <tt>grel</tt> expression to execute.
		 *
		 * @param expression the expression in <tt>grel</tt> to execute
		 * @return the builder for fluent usage
		 */
		public Builder grel(String expression) {
			this.expression = "grel:" + expression;
			return this;
		}

		/**
		 * Sets the <tt>jython</tt> expression to execute.
		 *
		 * @param expression the expression <tt>jython</tt> to execute
		 * @return the builder for fluent usage
		 */
		public Builder jython(String expression) {
			this.expression = "jython:" + expression;
			return this;
		}

		/**
		 * Sets the <tt>clojure</tt> expression to execute.
		 *
		 * @param expression the expression <tt>clojure</tt> to execute
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
