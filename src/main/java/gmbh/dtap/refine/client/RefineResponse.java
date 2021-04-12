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
