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
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static gmbh.dtap.refine.client.util.JsonParser.JSON_PARSER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Tests for {@link JsonParser}.
 */
public class JsonParserTest {

	private static final Charset UTF_8 = Charset.forName("UTF-8");

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void should_have_instance() {
		assertThat(JSON_PARSER).isNotNull();
	}

	@Test
	public void should_have_static_instance() {
		assertThat(JSON_PARSER).isEqualTo(JSON_PARSER);
	}

	@Test
	public void should_throw_exception_when_not_parsable_as_json() throws IOException, URISyntaxException {
		String plainText = IOUtils.toString(getClass().getResource("/responseBody/plain.txt").toURI(), UTF_8);

		thrown.expect(RefineException.class);
		JSON_PARSER.parseJson(plainText);
	}
}
