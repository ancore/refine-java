package com.ontotext.refine.client.cli;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.command.operations.GetOperationsResponse;
import com.ontotext.refine.client.exceptions.RefineException;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Parameters;


/**
 * Defines the export operations process and all of the required arguments for it.
 *
 * @author Antoniy Kunchev
 */
@Command(
    name = "exportOperations",
    description = "Exports the operations of specified project in JSON format.",
    version = "0.1.0",
    mixinStandardHelpOptions = true)
class ExportOperations extends Process {

  @Parameters(
      index = "0",
      paramLabel = "PROJECT",
      description = "The project which operations should be exported.")
  private String project;

  @Override
  public Integer call() throws Exception {
    try (RefineClient client = getClient()) {

      GetOperationsResponse response =
          RefineCommands.getOperations().setProject(project).build().execute(client);

      System.out.println(response.getContent());
      return ExitCode.OK;
    } catch (RefineException re) {
      System.err.println(re.getMessage());
    }
    return ExitCode.SOFTWARE;
  }

  /**
   * Runs the export operation process.
   *
   * @param args to pass to the process
   */
  public static void main(String[] args) {
    System.exit(new CommandLine(new ExportOperations()).execute(args));
  }
}
