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
