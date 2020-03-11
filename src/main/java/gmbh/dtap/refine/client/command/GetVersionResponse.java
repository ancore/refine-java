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

/**
 * This class represents the response from the {@link GetVersionCommand}.
 */
public class GetVersionResponse {

	private final String fullName;
	private final String fullVersion;
	private final String version;
	private final String revision;

	/**
	 * Contructor
	 *
	 * @param fullName    the full name
	 * @param fullVersion the full version
	 * @param version     the version
	 * @param revision    the revision
	 */
	GetVersionResponse(String fullName, String fullVersion, String version, String revision) {
		this.fullName = fullName;
		this.fullVersion = fullVersion;
		this.version = version;
		this.revision = revision;
	}

	/**
	 * Returns the full name.
	 *
	 * @return the full name
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Returns the full version.
	 *
	 * @return the full version
	 */
	public String getFullVersion() {
		return fullVersion;
	}

	/**
	 * Returns the version.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Returns the revision.
	 *
	 * @return the revision
	 */
	public String getRevision() {
		return revision;
	}

	@Override
	public String toString() {
		return getFullName();
	}
}
