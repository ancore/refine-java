package com.ontotext.refine.client.command;

import com.ontotext.refine.client.command.create.CreateProjectCommand;
import com.ontotext.refine.client.command.csrf.GetCsrfTokenCommand;
import com.ontotext.refine.client.command.delete.DeleteProjectCommand;
import com.ontotext.refine.client.command.export.ExportRowsCommand;
import com.ontotext.refine.client.command.models.GetProjectModelsCommand;
import com.ontotext.refine.client.command.operations.ApplyOperationsCommand;
import com.ontotext.refine.client.command.operations.GetOperationsCommand;
import com.ontotext.refine.client.command.preferences.GetPreferenceCommand;
import com.ontotext.refine.client.command.preferences.SetPreferenceCommand;
import com.ontotext.refine.client.command.processes.GetProcessesCommand;
import com.ontotext.refine.client.command.rdf.DefaultExportRdfCommand;
import com.ontotext.refine.client.command.rdf.SparqlBasedExportRdfCommand;
import com.ontotext.refine.client.command.reconcile.GuessColumnTypeCommand;
import com.ontotext.refine.client.command.reconcile.ReconServiceRegistrationCommand;
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
   * Provides a builder instance for the {@link DefaultExportRdfCommand}.
   *
   * @return new builder instance
   */
  static DefaultExportRdfCommand.Builder exportRdf() {
    return new DefaultExportRdfCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link SparqlBasedExportRdfCommand}.
   *
   * @return new builder instance
   */
  static SparqlBasedExportRdfCommand.Builder exportRdfUsingSparql() {
    return new SparqlBasedExportRdfCommand.Builder();
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

  /**
   * Provides a builder instance for the {@link ReconServiceRegistrationCommand}.
   *
   * @return new builder instance
   */
  static ReconServiceRegistrationCommand.Builder registerReconciliationService() {
    return new ReconServiceRegistrationCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link SetPreferenceCommand}.
   *
   * @return new builder instance
   */
  static SetPreferenceCommand.Builder setPreference() {
    return new SetPreferenceCommand.Builder();
  }

  /**
   * Provides a builder instance for the {@link GetPreferenceCommand}.
   *
   * @return new builder instance
   */
  static GetPreferenceCommand.Builder getPreference() {
    return new GetPreferenceCommand.Builder();
  }
}
