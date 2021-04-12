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

import gmbh.dtap.refine.client.RefineResponse;
import gmbh.dtap.refine.client.ResponseCode;

import java.util.Collections;
import java.util.List;

import static org.apache.http.util.Asserts.notEmpty;
import static org.apache.http.util.Asserts.notNull;

/**
 * This class represents the response from the <tt>expression preview</tt> request.
 */
public class ExpressionPreviewResponse extends RefineResponse {

	private List<String> expressionPreviews;

	/**
	 * Private constructor to enforce usage of factory methods.
	 *
	 * @param code               the code
	 * @param message            the message, may be {@code null}
	 * @param expressionPreviews the expression previews, may be empty but not {@code null}
	 */
	private ExpressionPreviewResponse(ResponseCode code, String message, List<String> expressionPreviews) {
		super(code, message);
		this.expressionPreviews = expressionPreviews;
	}

	/**
	 * Returns an instance to represent a success.
	 *
	 * @param expressionPreviews the list of expression previews
	 * @return the successful instance
	 */
	static ExpressionPreviewResponse ok(List<String> expressionPreviews) {
		notNull(expressionPreviews, "expressionPreviews");
		return new ExpressionPreviewResponse(ResponseCode.OK, null, expressionPreviews);
	}

	/**
	 * Returns an instance to represent an error.
	 *
	 * @param message the error message
	 * @return the error instance
	 */
	static ExpressionPreviewResponse error(String message) {
		notEmpty(message, "message");
		return new ExpressionPreviewResponse(ResponseCode.ERROR, message, Collections.emptyList());
	}

	/**
	 * Returns the results if response is successful.
	 *
	 * @return the list of the expression previews, may be empty but not {@code null}
	 */
	public List<String> getExpressionPreviews() {
		return expressionPreviews;
	}

	@Override public String toString() {
		return "ExpressionPreviewResponse{" +
			"expressionPreviews=" + expressionPreviews +
			'}';
	}
}
