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
import gmbh.dtap.refine.client.ResponseCode;
import org.apache.commons.io.IOUtils;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for {@link ExpressionPreviewCommand}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExpressionPreviewCommandTest {

	private static final Charset UTF_8 = Charset.forName("UTF-8");

	@Rule public ExpectedException thrown = ExpectedException.none();
	@Mock private RefineClient refineClient;

	private ExpressionPreviewCommand command;

	@Before
	public void setUp() throws MalformedURLException {
		refineClient = mock(RefineClient.class);
		when(refineClient.createUrl(anyString())).thenReturn(new URL("http://localhost:3333/"));
		command = RefineCommands.expressionPreview().project("1234567890").rowIndices(0).expression("foo").build();
	}

	@Test
	public void should_execute() throws IOException {
		command.execute(refineClient);
		verify(refineClient).createUrl(anyString());
		verify(refineClient).execute(any(), any());
	}

	@Test
	public void should_parse_expression_preview_success_response() throws IOException, URISyntaxException {
		String responseBody = IOUtils.toString(getClass().getResource("/responseBody/expression-preview.json").toURI(), UTF_8);

		ExpressionPreviewResponse response = command.parseExpressionPreviewResponse(responseBody);
		assertThat(response).isNotNull();
		assertThat(response.getCode()).isEqualTo(ResponseCode.OK);
		assertThat(response.getMessage()).isNull();
		assertThat(response.getExpressionPreviews()).containsExactly("7", "5", "5", "9");
	}

	@Test
	public void should_parse_expression_preview_error_response() throws IOException, URISyntaxException {
		String responseBody = IOUtils.toString(getClass().getResource("/responseBody/code-error.json").toURI(), UTF_8);

		ExpressionPreviewResponse response = command.parseExpressionPreviewResponse(responseBody);
		assertThat(response).isNotNull();
		assertThat(response.getCode()).isEqualTo(ResponseCode.ERROR);
		assertThat(response.getMessage()).isEqualTo("This is the error message.");
	}
}
