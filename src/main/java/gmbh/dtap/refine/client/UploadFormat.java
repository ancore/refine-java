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

package gmbh.dtap.refine.client;

/**
 * This enum is used to specify the format of an upload file when creating a {@link RefineProject}.
 * This is recommended if the file extension does not indicate the format clearly.
 */
public enum UploadFormat {

	/**
	 * line-based text file
	 */
	LINE_BASED("text/line-based"),

	/**
	 * CSV / TSV / separator-based file
	 * The separator to be used has to be submitted with {@link UploadOptions}.
	 */
	SEPARATOR_BASED("text/line-based/*sv"),

	/**
	 * fixed-width field text file
	 */
	FIXED_WIDTH_FIELD("text/line-based/fixed-width"),

	/**
	 * Excel file
	 */
	EXCEL("binary/text/xml/xls/xlsx"),

	/**
	 * JSON_PARSER file
	 */
	JSON("text/json"),

	/**
	 * XML file
	 */
	XML("text/xml");

	private final String value;

	UploadFormat(String value) {
		this.value = value;
	}

	/**
	 * Returns the textual value as expected by OpenRefine.
	 *
	 * @return the value to submit
	 */
	public String getValue() {
		return value;
	}
}
