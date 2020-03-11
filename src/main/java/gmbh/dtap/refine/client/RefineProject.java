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

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * This interface defines a project representation of the <a href="https://github.com/OpenRefine/OpenRefine/wiki/OpenRefine-API"></a>OpenRefine API</a>.
 */
public interface RefineProject {

	/**
	 * Returns the ID of the project.
	 *
	 * @return the project ID
	 */
	String getId();

	/**
	 * Returns the name of the project.
	 *
	 * @return the project name
	 */
	String getName();

	/**
	 * Return the URL of the project.
	 *
	 * @return the the project URL
	 */
	URL getUrl();

	/**
	 * Returns the project location, same as {@link RefineProject#getId()} and {@link RefineProject#getUrl()}.
	 *
	 * @return the project location
	 */
	ProjectLocation getLocation();

	/**
	 * Returns the creation date.
	 *
	 * @return the creation date
	 */
	OffsetDateTime getCreated();

	/**
	 * Returns the last modification date.
	 *
	 * @return the last modification date
	 */
	OffsetDateTime getModified();

	/**
	 * Returns the creator.
	 *
	 * @return the creator
	 */
	String getCreator();

	/**
	 * Returns the contributors.
	 *
	 * @return the contributors
	 */
	String getContributors();

	/**
	 * Returns the subject.
	 *
	 * @return the subject
	 */
	String getSubject();

	/**
	 * Returns the description.
	 *
	 * @return the description
	 */
	String getDescription();

	/**
	 * Returns the nuber of rows.
	 *
	 * @return the number of rows.
	 */
	long getRowCount();

	/**
	 * Returns custom metadata.
	 *
	 * @return the custom metadata
	 */
	CustomMetadata getCustomMetadata();

	/**
	 * Returns metadata from the import options.
	 *
	 * @return the import option metadata, never {@code null}
	 */
	List<ImportOptionMetadata> getImportOptionMetadata();
}
