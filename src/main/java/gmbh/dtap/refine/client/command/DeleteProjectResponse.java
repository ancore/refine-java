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

import static org.apache.http.util.Asserts.notEmpty;

/**
 * This class represents the response from the <code>delete project</code> request.
 */
public class DeleteProjectResponse extends RefineResponse {

	/**
	 * Private constructor to enforce usage of factory methods.
	 *
	 * @param code    the code
	 * @param message the message, may be {@code null}
	 */
	private DeleteProjectResponse(ResponseCode code, String message) {
		super(code, message);
	}

	/**
	 * Returns an instance to represent the success status.
	 *
	 * @return the success instance
	 */
	static DeleteProjectResponse ok() {
		return new DeleteProjectResponse(ResponseCode.OK, null);
	}

	/**
	 * Returns an instance to represent an error.
	 *
	 * @param message the error message
	 * @return the error instance
	 */
	static DeleteProjectResponse error(String message) {
		notEmpty(message, "message");
		return new DeleteProjectResponse(ResponseCode.ERROR, message);
	}
}
