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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gmbh.dtap.refine.client.RefineException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public enum JsonParser {

	JSON_PARSER;

	private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	;

	public <T> T read(String json, Class<T> type) throws IOException {
		T t = objectMapper.readValue(json, type);
		return type.cast(t);
	}

	public JsonNode parseJson(String json) throws IOException {
		try {
			return objectMapper.readTree(json);
		} catch (JsonProcessingException e) {
			throw new RefineException("Parser error: " + e.getMessage(), e);
		}
	}

	public List<String> toResults(JsonNode arrayNode) {
		List<String> resultList = new ArrayList<>();
		Iterator<JsonNode> iterator = arrayNode.elements();
		while (iterator.hasNext()) {
			JsonNode node = iterator.next();
			resultList.add(node.asText());
		}
		return resultList;
	}

	public JsonNode findExistingPath(JsonNode jsonNode, String path) throws IOException {
		JsonNode node = jsonNode.path(path);
		if (node.isMissingNode()) {
			throw new RefineException("Node with path '" + path + "' is missing: " + jsonNode);
		}
		return node;
	}
}
