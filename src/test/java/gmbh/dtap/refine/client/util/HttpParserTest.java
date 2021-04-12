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
