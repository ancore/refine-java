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

package gmbh.dtap.refine.demo;

import gmbh.dtap.refine.client.RefineClient;
import gmbh.dtap.refine.client.RefineClients;
import gmbh.dtap.refine.client.UploadFormat;
import gmbh.dtap.refine.client.command.CreateProjectResponse;
import gmbh.dtap.refine.client.command.DeleteProjectResponse;
import gmbh.dtap.refine.client.command.GetProjectMetadataResponse;
import gmbh.dtap.refine.client.command.RefineCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String... args) throws Exception {
		String options = "{ \"encoding\": \"UTF-8\", \"projectTags\": \"[foo]\", \"separator\": \",\"}";

		try (RefineClient client = RefineClients.create("http://localhost:3333")) {

			CreateProjectResponse createProjectResponse = RefineCommands
				.createProject()
				.name("CSV-Test (Main)")
				.file(new File("src/test/resources/addresses.csv"))
				.format(UploadFormat.SEPARATOR_BASED)
				.options(() -> options)
				.build()
				.execute(client);
			log.info("createProjectResponse: {}", createProjectResponse);

			try {
				GetProjectMetadataResponse projectMetadataResponse = RefineCommands.getProjectMetadataCommand()
					.project(createProjectResponse.getProjectId())
					.build()
					.execute(client);
				log.info("projectMetadata: {}", projectMetadataResponse.getProjectMetadata());
			} catch (Exception e) {
				log.error("projectMetadata failed", e);
			}

			System.in.read();

			DeleteProjectResponse deleteProjectResponse = RefineCommands.deleteProject()
				.project(createProjectResponse.getProjectId())
				.build()
				.execute(client);

			log.info("deleteProjectResponse: {}", deleteProjectResponse);
		}
	}
}
