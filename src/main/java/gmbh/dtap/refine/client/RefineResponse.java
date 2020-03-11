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

package gmbh.dtap.refine.client;

/**
 * This class contains fields from a Refine server response JSON document.
 */
public abstract class RefineResponse {

	private final ResponseCode code;
	private final String message;

	/**
	 * Constructor.
	 *
	 * @param code    the code field
	 * @param message the message field, may be {@code null}
	 */
	public RefineResponse(ResponseCode code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * Returns the code from the response document.
	 *
	 * @return the code
	 */
	public ResponseCode getCode() {
		return code;
	}

	/**
	 * Returns the error message from the response document.
	 *
	 * @return the error message, may be {@code null}
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append("{");
		sb.append("code=").append(code);
		if (message != null) {
			sb.append(", message='").append(message).append('\'');
		}
		sb.append('}');
		return sb.toString();
	}
}
