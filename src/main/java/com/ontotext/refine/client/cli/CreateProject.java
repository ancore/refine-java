package com.ontotext.refine.client.cli;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.UploadFormat;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.command.create.CreateProjectResponse;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.File;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;


/**
 * Defines the create project process and all of the required arguments for it.
 *
 * @author Antoniy Kunchev
 */
@Command(
    name = "create",
    description = "Creates a new project from a file.",
    version = "0.1.0",
    mixinStandardHelpOptions = true)
class CreateProject extends Process {

  @Parameters(
      index = "0",
      paramLabel = "FILE",
      description = "The file which will be used to create the project."
          + " It should be full name with extension (csv, tsv, xml, json, txt, xls, xlsx, ods).")
  private File file;

  @Option(
      names = {"-n", "--name"},
      description = "The name with which the project will be created. If not provided,"
          + " the file name will be used.")
  private String name;

  @Option(
      names = {"-f", "--format"},
      description = "The format of the provided file.",
      defaultValue = "text/line-based/*sv")
  private String format;

  @Override
  public Integer call() throws Exception {
    try (RefineClient client = getClient()) {
      CreateProjectResponse response = RefineCommands
          .createProject()
          .file(file)
          .format(UploadFormat.resolve(format))
          .name(StringUtils.defaultIfBlank(name, file.getName()))
          .token(getToken())
          .build()
          .execute(client);

      String message = String.format(
          "The project with id '%s' was created successfully.", response.getProjectId());
      System.out.println(message);
      return ExitCode.OK;
    } catch (RefineException re) {
      System.err.println(re.getMessage());
    }
    return ExitCode.SOFTWARE;
  }

  /**
   * Runs the creation of project command.
   *
   * @param args to pass to the process
   */
  public static void main(String[] args) {
    System.exit(new CommandLine(new CreateProject()).execute(args));
  }
}
