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

package gmbh.dtap.refine.client.command;

import gmbh.dtap.refine.client.ProjectMetadata;

public class GetProjectMetadataResponse {

	private final ProjectMetadata projectMetadata;

	public GetProjectMetadataResponse(ProjectMetadata projectMetadata) {
		this.projectMetadata = projectMetadata;
	}

	public ProjectMetadata getProjectMetadata() {
		return projectMetadata;
	}

	@Override public String toString() {
		return "GetProjectMetadataResponse{" +
			"projectMetadata=" + projectMetadata +
			'}';
	}
}
