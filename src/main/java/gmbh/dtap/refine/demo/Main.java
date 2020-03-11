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
