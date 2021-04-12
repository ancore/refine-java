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

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * This interface defines a project representation of the <a href="https://github.com/OpenRefine/OpenRefine/wiki/OpenRefine-API">OpenRefine API</a>.
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
