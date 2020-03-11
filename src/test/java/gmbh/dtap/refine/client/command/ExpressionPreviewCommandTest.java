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
