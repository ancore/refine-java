package com.ontotext.refine.client.cli;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.ResponseCode;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.command.processes.GetProcessesCommand;
import com.ontotext.refine.client.command.processes.GetProcessesCommandResponse.ProjectProcess;
import com.ontotext.refine.client.command.reconcile.GuessColumnTypeCommandResponse;
import com.ontotext.refine.client.command.reconcile.GuessColumnTypeCommandResponse.ReconciliationType;
import com.ontotext.refine.client.command.reconcile.ReconcileCommand.ColumnType;
import com.ontotext.refine.client.command.reconcile.ReconcileCommandResponse;
import com.ontotext.refine.client.exceptions.RefineException;
import java.util.Collection;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;


/**
 * Defines a reconciliation process and all of the required arguments for it.
 *
 * @author Antoniy Kunchev
 */
@Command(
    name = "reconcile",
    description = "Performs reconciliation over the project data.",
    version = "0.1.0",
    mixinStandardHelpOptions = true)
class Reconcile extends Process {

  private static final int SLEEP_T = 2000;

  @Parameters(
      index = "0",
      arity = "1",
      paramLabel = "PROJECT",
      description = "The identifier of the project which should be reconciled.")
  private String project;

  @Parameters(
      index = "1",
      arity = "1",
      paramLabel = "COLUMN",
      description = "The identifier of the column that should be reconciled.")
  private String column;

  @Parameters(
      index = "2",
      arity = "1",
      paramLabel = "SERVICE",
      description = "The service that should be used for the column reconciliation.")
  private String service;

  @Option(
      names = {"-a", "--async"},
      description = "Whether to execute the operation asynchronous and output the progress.",
      defaultValue = "false")
  private Boolean async;

  @Override
  public Integer call() throws Exception {
    try (RefineClient client = getClient()) {

      ReconciliationType columnType = guessColumnType(client);

      ReconcileCommandResponse response = reconcile(client, columnType);

      if (ResponseCode.ERROR.equals(response.getCode())) {
        System.err.printf(
            "Failed to reconcile column '%s' for project '%s' due to: %s",
            column,
            project,
            response.getMessage(),
            System.lineSeparator());
        return ExitCode.SOFTWARE;
      }

      if (async) {
        System.out.println(
            "The reconciliation process for column '" + column
                + "' is currently running in asynchronous mode.");
        return ExitCode.OK;
      }

      printProgress(client);
      return ExitCode.OK;
    } catch (RefineException re) {
      System.err.println(re.getMessage());
    }
    return ExitCode.SOFTWARE;
  }

  private void printProgress(RefineClient client) throws RefineException {
    GetProcessesCommand command = RefineCommands.getProcesses().setProject(project).build();
    Collection<ProjectProcess> processes = command.execute(client).getProcesses();
    while (!processes.isEmpty()) {
      ProjectProcess process = findExactProcess(processes);
      if (process != null) {
        int progress = process.getProgress();
        System.out.print(
            "Reconciliation of column '" + column + "' is in progress... " + progress + "%\r");
      } else {
        System.out.println(
            "Can't track the progress of the reconciliation process. "
                + "The process is most likely still running in background.");
        return;
      }

      sleep();
      processes = command.execute(client).getProcesses();
    }

    System.out.print("Reconciliation of column '" + column + "' is in progress... 100%\r");
    System.out.println(System.lineSeparator() + "Done.");
  }

  private ProjectProcess findExactProcess(Collection<ProjectProcess> processes) {
    for (ProjectProcess process : processes) {
      String description = process.getDescription();
      if (description.startsWith("Reconcile") && description.contains("column " + column)) {
        return process;
      }
    }
    return null;
  }

  private void sleep() {
    try {
      Thread.sleep(SLEEP_T);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
    }
  }

  private ReconciliationType guessColumnType(RefineClient client) throws RefineException {
    System.out.println("Guessing the type of the column. It may take a while...");

    GuessColumnTypeCommandResponse response = RefineCommands
        .guessTypeOfColumn()
        .setProject(project)
        .setColumn(column)
        .setService(service)
        .setToken(getToken())
        .build()
        .execute(client);

    if (response.getError() != null) {
      throw new RefineException(
          "Failed to guess the type of the column: '%s' for project: '%s' due to: %s",
          column,
          project,
          response.getError());
    }

    if (response.getTypes().isEmpty()) {
      throw new RefineException(
          "Failed to guess the type of the column: '%s' for project: '%s'", column, project);
    }

    return response.getTypes().get(0);
  }

  private ReconcileCommandResponse reconcile(RefineClient client, ReconciliationType columnType)
      throws RefineException {
    return RefineCommands
        .reconcile()
        .setProject(project)
        .setColumn(column)
        .setColumnType(new ColumnType(columnType.getId(), columnType.getName()))
        .setToken(getToken())
        .build()
        .execute(client);
  }

  /**
   * Runs the reconcile operation process.
   *
   * @param args to pass to the process
   */
  public static void main(String[] args) {
    System.exit(new CommandLine(new Reconcile()).execute(args));
  }
}
