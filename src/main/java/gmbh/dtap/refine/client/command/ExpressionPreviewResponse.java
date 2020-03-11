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
