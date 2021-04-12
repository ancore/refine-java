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

import java.io.IOException;

/**
 * This exception is thrown when the server responds with an error or unexpected response,
 * and when the response can not be understood.
 */
public class RefineException extends IOException {

	private static final long serialVersionUID = 5368187574797784277L;

	public RefineException(String message) {
		super(message);
	}

	public RefineException(String message, Throwable cause) {
		super(message, cause);
	}

	public RefineException(Throwable cause) {
		super(cause);
	}
}
