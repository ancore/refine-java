/*
 * Copyright 2019 DTAP GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ontotext.refine.client.command;

public interface RefineCommands {

  static ApplyOperationsCommand.Builder applyOperations() {
    return new ApplyOperationsCommand.Builder();
  }

  static CreateProjectCommand.Builder createProject() {
    return new CreateProjectCommand.Builder();
  }

  static DeleteProjectCommand.Builder deleteProject() {
    return new DeleteProjectCommand.Builder();
  }

  static ExpressionPreviewCommand.Builder expressionPreview() {
    return new ExpressionPreviewCommand.Builder();
  }

  static GetVersionCommand.Builder getVersion() {
    return new GetVersionCommand.Builder();
  }

  static GetProjectMetadataCommand.Builder getProjectMetadataCommand() {
    return new GetProjectMetadataCommand.Builder();
  }

  static GetCsrfTokenCommand.Builder getCsrfToken() {
    return new GetCsrfTokenCommand.Builder();
  }

  static ExportRowsCommand.Builder exportRows() {
    return new ExportRowsCommand.Builder();
  }
}
