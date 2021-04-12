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

import gmbh.dtap.refine.client.RefineClient;
import gmbh.dtap.refine.client.RefineException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import static gmbh.dtap.refine.client.testsupport.HttpMock.mockHttpResponse;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for {@link GetVersionCommand}.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetVersionCommandTest {

	private static final Charset UTF_8 = Charset.forName("UTF-8");

	@Rule public ExpectedException thrown = ExpectedException.none();
	@Mock private RefineClient refineClient;

	private GetVersionCommand command;

	@Before
	public void setUp() throws MalformedURLException {
		refineClient = mock(RefineClient.class);
		when(refineClient.createUrl(anyString())).thenReturn(new URL("http://localhost:3333/"));
		command = RefineCommands.getVersion().build();
	}

	@Test
	public void should_execute() throws IOException {
		command.execute(refineClient);
		verify(refineClient).createUrl(anyString());
		verify(refineClient).execute(any(), any());
	}

	@Test
	public void should_parse_get_version_success_response() throws IOException, URISyntaxException {
		String responseBody = IOUtils.toString(getClass().getResource("/responseBody/get-version.json").toURI(), UTF_8);

		GetVersionResponse response = command.parseGetVersionResponse(responseBody);
		assertThat(response).isNotNull();
		assertThat(response.getFullName()).isEqualTo("OpenRefine 3.0-beta [TRUNK]");
		assertThat(response.getFullVersion()).isEqualTo("3.0-beta [TRUNK]");
		assertThat(response.getVersion()).isEqualTo("3.0-beta");
		assertThat(response.getRevision()).isEqualTo("TRUNK");
	}

	@Test
	public void should_throw_exception_when_not_parsable_as_get_version_response() throws IOException, URISyntaxException {
		String responseBody = IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);

		thrown.expect(RefineException.class);
		command.parseGetVersionResponse(responseBody);
	}

	@Test
	public void should_throw_exception_when_response_status_is_500() throws IOException {
		HttpResponse httpResponse = mockHttpResponse(500);

		thrown.expect(RefineException.class);
		command.handleResponse(httpResponse);
	}

	@Test
	public void should_throw_exception_when_response_body_is_no_json() throws IOException, URISyntaxException {
		String responseBody = IOUtils.toString(getClass().getResource("/responseBody/plain.txt").toURI(), UTF_8);
		HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

		thrown.expect(RefineException.class);
		command.handleResponse(httpResponse);
	}

	@Test
	public void should_throw_exception_when_not_parsable() throws IOException, URISyntaxException {
		String responseBody = IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);
		HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

		thrown.expect(RefineException.class);
		command.handleResponse(httpResponse);
	}

	@Test
	public void should_return_success_when_response_is_positive() throws IOException, URISyntaxException {
		String responseBody = IOUtils.toString(getClass().getResource("/responseBody/get-version.json").toURI(), UTF_8);
		HttpResponse httpResponse = mockHttpResponse(200, APPLICATION_JSON, responseBody);

		GetVersionResponse response = command.handleResponse(httpResponse);
		assertThat(response).isNotNull();
		assertThat(response.getFullName()).isEqualTo("OpenRefine 3.0-beta [TRUNK]");
		assertThat(response.getFullVersion()).isEqualTo("3.0-beta [TRUNK]");
		assertThat(response.getVersion()).isEqualTo("3.0-beta");
		assertThat(response.getRevision()).isEqualTo("TRUNK");
	}
}
