package com.ontotext.refine.client.cli;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.ResponseCode;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.command.delete.DeleteProjectResponse;
import com.ontotext.refine.client.exceptions.RefineException;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Parameters;


/**
 * Defines the delete project process and all of the required arguments for it.
 *
 * @author Antoniy Kunchev
 */
@Command(
    name = "delete",
    description = "Deletes specific project from OntoRefine.",
    version = "0.1.0",
    mixinStandardHelpOptions = true)
class DeleteProject extends Process {

  @Parameters(
      index = "0",
      paramLabel = "PROJECT",
      description = "The identifier of the project which should be deleted.")
  private String project;

  @Override
  public Integer call() throws Exception {
    try (RefineClient client = getClient()) {
      DeleteProjectResponse response = RefineCommands.deleteProject()
          .project(project)
          .token(getToken())
          .build()
          .execute(client);

      if (ResponseCode.ERROR.equals(response.getCode())) {
        String error = String.format(
            "Failed to delete project with id '%s' due to: %s",
            project,
            response.getMessage());
        System.err.println(error);
        return ExitCode.SOFTWARE;
      }

      System.out.println(String.format("Successfully deleted project with id: '%s'", project));
      return ExitCode.OK;
    } catch (RefineException re) {
      System.err.println(re.getMessage());
    }
    return ExitCode.SOFTWARE;
  }

  /**
   * Runs the deletion of project command.
   *
   * @param args to pass to the process
   */
  public static void main(String[] args) {
    System.exit(new CommandLine(new DeleteProject()).execute(args));
  }
}
