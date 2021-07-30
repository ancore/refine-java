/*
 * Copyright 2019 DTAP GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ontotext.refine.client.util;

import static com.ontotext.refine.client.util.HttpParser.HTTP_PARSER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ontotext.refine.client.exceptions.RefineException;
import com.ontotext.refine.client.testsupport.HttpMock;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.Test;


/**
 * Unit Tests for {@link HttpParser}.
 */
class HttpParserTest {

  @Test
  void should_have_instance() {
    // this make no sense
    assertNotNull(HTTP_PARSER);
  }

  @Test
  void should_throw_exception_when_status_code_not_assured() throws IOException {
    HttpResponse badRequestResponse = HttpMock.mockHttpResponse(500);

    assertThrows(RefineException.class,
        () -> HTTP_PARSER.assureStatusCode(badRequestResponse, 200));
  }

  @Test
  void should_not_throw_exception_when_status_code_is_assured() throws IOException {
    HttpResponse okResponse = HttpMock.mockHttpResponse(200);

    assertDoesNotThrow(() -> HTTP_PARSER.assureStatusCode(okResponse, 200));
  }
}
