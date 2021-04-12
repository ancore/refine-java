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
