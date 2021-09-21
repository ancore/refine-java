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

import com.ontotext.refine.client.command.create.CreateProjectCommand;
import com.ontotext.refine.client.command.csrf.GetCsrfTokenCommand;
import com.ontotext.refine.client.command.delete.DeleteProjectCommand;
import com.ontotext.refine.client.command.models.GetProjectModelsCommand;
import com.ontotext.refine.client.command.operations.ApplyOperationsCommand;
import com.ontotext.refine.client.command.operations.GetOperationsCommand;
import com.ontotext.refine.client.command.processes.GetProcessesCommand;
import com.ontotext.refine.client.command.rdf.ExportRdfCommand;
import com.ontotext.refine.client.command.reconcile.GuessColumnTypeCommand;
import com.ontotext.refine.client.command.reconcile.ReconcileCommand;
import com.ontotext.refine.client.command.version.GetVersionCommand;


/**
 * Provides all of the available commands for the Refine tool.
 */
public interface RefineCommands {

  /**
   * Provides a builder instance for the {@link ApplyOperationsCommand}.
   *
   * @return new builder instance
   */
  static ApplyOperationsCommand.Builder applyOperations() {
    return new ApplyOperationsCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link CreateProjectCommand}.
   *
   * @return new builder instance
   */
  static CreateProjectCommand.Builder createProject() {
    return new CreateProjectCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link DeleteProjectCommand}.
   *
   * @return new builder instance
   */
  static DeleteProjectCommand.Builder deleteProject() {
    return new DeleteProjectCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link ExpressionPreviewCommand}.
   *
   * @return new builder instance
   */
  static ExpressionPreviewCommand.Builder expressionPreview() {
    return new ExpressionPreviewCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link GetVersionCommand}.
   *
   * @return new builder instance
   */
  static GetVersionCommand.Builder getVersion() {
    return new GetVersionCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link GetProjectMetadataCommand}.
   *
   * @return new builder instance
   */
  static GetProjectMetadataCommand.Builder getProjectMetadataCommand() {
    return new GetProjectMetadataCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link GetCsrfTokenCommand}.
   *
   * @return new builder instance
   */
  static GetCsrfTokenCommand.Builder getCsrfToken() {
    return new GetCsrfTokenCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link ExportRowsCommand}.
   *
   * @return new builder instance
   */
  static ExportRowsCommand.Builder exportRows() {
    return new ExportRowsCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link GetOperationsCommand}.
   *
   * @return new builder instance
   */
  static GetOperationsCommand.Builder getOperations() {
    return new GetOperationsCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link ExportRdfCommand}.
   *
   * @return new builder instance
   */
  static ExportRdfCommand.Builder exportRdf() {
    return new ExportRdfCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link GuessColumnTypeCommand}.
   *
   * @return new builder instance
   */
  static GuessColumnTypeCommand.Builder guessTypeOfColumn() {
    return new GuessColumnTypeCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link GetProcessesCommand}.
   *
   * @return new builder instance
   */
  static GetProcessesCommand.Builder getProcesses() {
    return new GetProcessesCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link ReconcileCommand}.
   *
   * @return new builder instance
   */
  static ReconcileCommand.Builder reconcile() {
    return new ReconcileCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link GetProjectModelsCommand}.
   *
   * @return new builder instance
   */
  static GetProjectModelsCommand.Builder getProjectModels() {
    return new GetProjectModelsCommand.Builder();
  }
}
