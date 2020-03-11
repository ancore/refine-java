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

package gmbh.dtap.refine.client.util;

import gmbh.dtap.refine.client.RefineException;
import gmbh.dtap.refine.client.testsupport.HttpMock;
import org.apache.http.HttpResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static gmbh.dtap.refine.client.util.HttpParser.HTTP_PARSER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Tests for {@link HttpParser}.
 */
public class HttpParserTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void should_have_instance() {
		assertThat(HTTP_PARSER).isNotNull();
	}

	@Test
	public void should_have_static_instance() {
		assertThat(HTTP_PARSER).isEqualTo(HTTP_PARSER);
	}

	@Test
	public void should_throw_exception_when_status_code_not_assured() throws IOException {
		HttpResponse badRequestResponse = HttpMock.mockHttpResponse(500);

		thrown.expect(RefineException.class);
		HTTP_PARSER.assureStatusCode(badRequestResponse, 200);
	}

	@Test
	public void should_not_throw_exception_when_status_code_is_assured() throws IOException {
		HttpResponse okResponse = HttpMock.mockHttpResponse(200);

		HTTP_PARSER.assureStatusCode(okResponse, 200);
	}
}
