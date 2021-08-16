package com.ontotext.refine.client.cli;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.command.rdf.ExportRdfResponse;
import com.ontotext.refine.client.exceptions.RefineException;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Parameters;


/**
 * Defines the export RDF process and all of the required arguments for it.
 *
 * @author Antoniy Kunchev
 */
@Command(
    name = "exportRdf",
    description = "Exports the data of specified project in RDF format.",
    version = "0.1.0",
    mixinStandardHelpOptions = true)
class ExportRdf extends Process {

  @Parameters(
      index = "0",
      paramLabel = "PROJECT",
      description = "The project which data should be exported.")
  private String project;

  @Override
  public Integer call() {
    try (RefineClient client = getClient()) {

      ExportRdfResponse response =
          RefineCommands.exportRdf().setProject(project).build().execute(client);

      System.out.println(response.getResult());
      return ExitCode.OK;
    } catch (RefineException re) {
      System.err.println(re.getMessage());
    } catch (Exception exc) {
      String error = String.format(
          "Failed to execute RDF export for project: '%s' due to %s", project, exc.getMessage());
      System.err.println(error);
    }
    return ExitCode.SOFTWARE;
  }

  /**
   * Runs the export RDF process.
   *
   * @param args to pass to the process
   */
  public static void main(String[] args) {
    System.exit(new CommandLine(new ExportRdf()).execute(args));
  }
}
