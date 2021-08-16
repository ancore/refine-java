package com.ontotext.refine.client.cli;

import com.ontotext.refine.client.JsonOperation;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.ResponseCode;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.command.operations.ApplyOperationsResponse;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Parameters;


/**
 * Defines a apply operations process and all of the required arguments for it.
 *
 * @author Antoniy Kunchev
 */
@Command(
    name = "apply",
    description = "Applies transformation operations to specific project.",
    version = "0.1.0",
    mixinStandardHelpOptions = true)
class ApplyOperations extends Process {

  @Parameters(
      index = "0",
      arity = "1",
      paramLabel = "OPERATIONS",
      description = "The file with the operations that should be applied to the project."
          + " The file should be a JSON file.")
  private File operations;

  @Parameters(
      index = "1",
      paramLabel = "PROJECT",
      description = "The identifier of the project to which the transformation"
          + " operations will be applied.")
  private String project;

  @Override
  public Integer call() throws Exception {
    try (RefineClient client = getClient()) {
      String ops = IOUtils.toString(new FileReader(operations, StandardCharsets.UTF_8));

      ApplyOperationsResponse response = RefineCommands
          .applyOperations()
          .project(project)
          .token(getToken())
          .operations(JsonOperation.from(ops))
          .build()
          .execute(client);

      if (ResponseCode.ERROR.equals(response.getCode())) {
        String error = String.format(
            "Failed to apply transformation to the project '%s' due to: %s",
            project,
            response.getMessage());

        System.err.println(error);
        return ExitCode.SOFTWARE;
      }

      System.out.println("The transformations were successfully applied to project: " + project);
      return ExitCode.OK;
    } catch (RefineException re) {
      System.err.println(re.getMessage());
    }
    return ExitCode.SOFTWARE;
  }

  /**
   * Runs the apply operations command.
   *
   * @param args to pass to the process
   */
  public static void main(String[] args) {
    System.exit(new CommandLine(new ApplyOperations()).execute(args));
  }
}
