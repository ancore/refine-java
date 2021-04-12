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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Tests for {@link RefineResponse}.
 */
public class RefineResponseTest {

	/**
	 * Test implementation for abstract RefineResponse.
	 */
	class TestRefineResponse extends RefineResponse {
		TestRefineResponse(ResponseCode code, String message) {
			super(code, message);
		}
	}

	@Test
	public void should_have_fields_from_constructor() {
		RefineResponse refineResponse = new TestRefineResponse(ResponseCode.OK, "Test");
		assertThat(refineResponse.getCode()).isEqualTo(ResponseCode.OK);
		assertThat(refineResponse.getMessage()).isEqualTo("Test");
	}

	@Test
	public void should_have_null_fields_from_constructor() {
		RefineResponse refineResponse = new TestRefineResponse(null, null);
		assertThat(refineResponse.getCode()).isNull();
		assertThat(refineResponse.getMessage()).isNull();
	}
}
