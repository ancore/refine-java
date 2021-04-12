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

/**
 * Defines the project URL. THe ID is part of the URL and provided additionally by its onw getter.
 * <p>
 * When creating a project, the server only responds with the location (URL) of the new project.
 * s
 */
public interface ProjectLocation {

	/**
	 * Returns the ID of the project. The ID is present as query parameter <code>project</code> in the URL also.
	 *
	 * @return the project ID
	 */
	String getId();

	/**
	 * Return the URL of the project.
	 *
	 * @return the the project URL
	 */
	URL getUrl();
}
