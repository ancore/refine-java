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
