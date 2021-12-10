package com.ontotext.refine.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.command.create.CreateProjectResponse;
import com.ontotext.refine.client.command.delete.DeleteProjectResponse;
import com.ontotext.refine.client.command.export.AdditionalExportConfigs;
import com.ontotext.refine.client.command.export.ExportRowsResponse;
import com.ontotext.refine.client.command.operations.ApplyOperationsResponse;
import com.ontotext.refine.client.command.rdf.ExportRdfResponse;
import com.ontotext.refine.client.command.rdf.ResultFormat;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

/**
 * Integration tests related to export commands. The test contains different tests which are
 * executing their own scenario and verifying the result. The scenarios are combining execution of
 * different commands. The tests is using real GraphDB with OntoRefine instance.
 *
 * @author Antoniy Kunchev
 * @see IntegrationTest
 */
class ExportIntegrationTests extends CommandIntegrationTest {

  private static final String OPERATIONS_JSON =
      "/integration/netherlands_restaurants_operations.json";
  private static final String RESTAURANTS_CSV = "integration/reduced_netherlands_restaurants.csv";

  /**
   * Note the scenario uses reduced dataset in order to complete the operations over it quickly!
   *
   * <p>Scenario 1:<br>
   * <br>
   * 1. Creates project with name 'Scenario-1-{test-class-name}' using the
   * 'reduced_netherlands_resturants.csv'<br>
   * 2. Apply transformation script to the project data<br>
   * 3. Wait until all operations are applied<br>
   * 4. Export the data in CSV format<br>
   * 5. Delete the project
   */
  @Test
  void exportCsv() throws Exception {
    String projectName = "Scenario-1-" + ExportIntegrationTests.class.getSimpleName();
    CreateProjectResponse createResponse = createProject(projectName, RESTAURANTS_CSV);
    String projectId = createResponse.getProjectId();
    assertNotNull(projectId);

    ApplyOperationsResponse applyOperationsResponse = applyOperations(projectId, OPERATIONS_JSON);
    assertNotEquals(ResponseCode.ERROR, applyOperationsResponse.getCode());

    waitForProcessesCompletion(projectId);

    ExportRowsResponse exportResponse = exportRows(projectId, "csv");

    String expected = IOUtils.resourceToString(
        "/integration/expected/scenario_1_reduced_netherlands_restaurants_export.csv",
        StandardCharsets.UTF_8);
    File file = exportResponse.getFile();
    assertEquals(expected, FileUtils.readFileToString(file, StandardCharsets.UTF_8));
    Files.delete(file.toPath());

    DeleteProjectResponse deleteResponse = deleteProject(projectId);
    assertEquals(ResponseCode.OK, deleteResponse.getCode());
  }

  private ApplyOperationsResponse applyOperations(String projectId, String filePath)
      throws IOException {
    String operationStr = IOUtils.resourceToString(filePath, StandardCharsets.UTF_8);
    Operation operations = JsonOperation.from(operationStr);
    return RefineCommands
        .applyOperations()
        .project(projectId)
        .operations(operations)
        .token(getToken())
        .build()
        .execute(getClient());
  }

  private ExportRowsResponse exportRows(String projectId, String format) throws RefineException {
    return RefineCommands
        .exportRows()
        .setProject(projectId)
        .setFormat(format)
        .setToken(getToken())
        .setExportConfigs(AdditionalExportConfigs.createDefault().setTruncateFile(true))
        .build()
        .execute(getClient());
  }

  /**
   * Note the scenario uses reduced dataset in order to complete the operations over it quickly!
   *
   * <p>Scenario 2:<br>
   * <br>
   * 1. Creates project with name 'Scenario-2-{test-class-name}' using the
   * 'reduced_netherlands_resturants.csv'<br>
   * 2. Apply transformation script to the project data<br>
   * 3. Wait until all operations are applied<br>
   * 4. Export the data in RDF format with specific mapping<br>
   * 5. Delete the project
   */
  @Test
  void exportRdf() throws Exception {
    String projectName = "Scenario-2-" + ExportIntegrationTests.class.getSimpleName();
    CreateProjectResponse createResponse = createProject(projectName, RESTAURANTS_CSV);
    String projectId = createResponse.getProjectId();
    assertNotNull(projectId);

    ApplyOperationsResponse applyOperationsResponse = applyOperations(projectId, OPERATIONS_JSON);
    assertNotEquals(ResponseCode.ERROR, applyOperationsResponse.getCode());

    waitForProcessesCompletion(projectId);

    ExportRdfResponse exportRdfResponse = exportAsRdf(projectId);

    String expected = IOUtils.resourceToString(
        "/integration/expected/scenario_2_reduced_netherlands_restaurants_exportRdf.ttl",
        StandardCharsets.UTF_8);

    // the files cannot be compared directly because of the IRI generation
    assertEquals(expected.length(), exportRdfResponse.getResult().length());

    DeleteProjectResponse deleteResponse = deleteProject(projectId);
    assertEquals(ResponseCode.OK, deleteResponse.getCode());
  }

  private ExportRdfResponse exportAsRdf(String projectId) throws IOException {
    String mapping = IOUtils.resourceToString(
        "/integration/netherlands_restaurants_rdf_mapping.json",
        StandardCharsets.UTF_8);
    return RefineCommands
        .exportRdf()
        .setProject(projectId)
        .setFormat(ResultFormat.TURTLE)
        .setMapping(mapping)
        .build()
        .execute(getClient());
  }
}
