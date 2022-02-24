package com.ontotext.refine.client;

import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.command.create.CreateProjectCommand;
import com.ontotext.refine.client.command.create.CreateProjectResponse;
import com.ontotext.refine.client.command.delete.DeleteProjectResponse;
import com.ontotext.refine.client.command.processes.GetProcessesCommand;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.File;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import org.testcontainers.shaded.org.awaitility.Awaitility;

/**
 * Base class for integration testing of the {@link RefineCommands}. It extends
 * {@link IntegrationTest} and provides additional convenient methods which can be reused in the
 * concrete tests implementations.
 *
 * @author Antoniy Kunchev
 * @see IntegrationTest
 */
public abstract class CommandIntegrationTest extends IntegrationTest {

  private static final long WAIT_FOR_PROCESSES = 20;

  // For transparency reason
  @Override
  protected void security(Turn turn) {
    super.security(turn);
  }

  /**
   * Provides CSRF tokens, retrieved from OntoRefine tool.
   *
   * @return CSRF token
   */
  protected String getToken() {
    try {
      return RefineCommands.getCsrfToken().build().execute(getClient()).getToken();
    } catch (RefineException re) {
      throw new AssertionError("Failed to retrieve CSRF token due to: " + re.getMessage(), re);
    }
  }

  /**
   * Creates project in OntoRefine using {@link CreateProjectCommand} with specific name and
   * dataset.
   *
   * @param projectName name to be used for the project
   * @param filePath of the dataset for the project
   * @return the response from the operation
   */
  protected CreateProjectResponse createProject(String projectName, String filePath)
      throws RefineException, URISyntaxException {
    return RefineCommands
        .createProject()
        .name(projectName)
        .file(new File(getClass().getClassLoader().getResource(filePath).toURI()))
        .format(UploadFormat.SEPARATOR_BASED)
        .token(getToken())
        .build()
        .execute(getClient());
  }

  /**
   * Waits until all processes (if any) are completed for the given project. The method will pull
   * the processes that are currently executed from the OntoRefine for the project every
   * {@value #WAIT_FOR_PROCESSES} seconds until there are no processes returned.
   *
   * @param projectId which processes should be completed
   */
  protected void waitForProcessesCompletion(String projectId) throws RefineException {
    GetProcessesCommand command = RefineCommands.getProcesses().setProject(projectId).build();
    Awaitility.await()
        .atMost(WAIT_FOR_PROCESSES, TimeUnit.SECONDS)
        .until(() -> command.execute(getClient()).getProcesses().isEmpty());
  }

  /**
   * Deletes project in OntoRefine by specific project identifier.
   *
   * @param projectId identifier of the project that should be deleted
   * @return the response from the operation
   */
  protected DeleteProjectResponse deleteProject(String projectId) throws RefineException {
    return RefineCommands
        .deleteProject()
        .project(projectId)
        .token(getToken())
        .build()
        .execute(getClient());
  }
}
