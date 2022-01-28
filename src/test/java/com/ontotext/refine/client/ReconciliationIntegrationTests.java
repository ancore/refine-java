package com.ontotext.refine.client;

import static com.ontotext.refine.client.util.JsonParser.JSON_PARSER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.command.create.CreateProjectResponse;
import com.ontotext.refine.client.command.operations.GetOperationsResponse;
import com.ontotext.refine.client.command.preferences.GetPreferenceCommandResponse;
import com.ontotext.refine.client.command.reconcile.GuessColumnTypeCommandResponse;
import com.ontotext.refine.client.command.reconcile.GuessColumnTypeCommandResponse.ReconciliationType;
import com.ontotext.refine.client.command.reconcile.ReconServiceRegistrationCommandResponse;
import com.ontotext.refine.client.command.reconcile.ReconcileCommand.ColumnType;
import com.ontotext.refine.client.command.reconcile.ReconcileCommandResponse;
import com.ontotext.refine.client.exceptions.RefineException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * Integration tests related to reconciliation commands. The test contains different tests which are
 * executing their own scenario and verifying the result. The scenarios are combining execution of
 * different commands. The tests is using real GraphDB with OntoRefine instance.
 *
 * @author Antoniy Kunchev
 * @see IntegrationTest
 */
class ReconciliationIntegrationTests extends CommandIntegrationTest {

  private static final String RECON_SERVICE = "https://reconcile.ontotext.com/organizations";
  private static final String RESTAURANTS_CSV = "integration/reduced_netherlands_restaurants.csv";

  /**
   * Scenario 1:<br>
   * <br>
   * 1. Register {@value #RECON_SERVICE} as additional service in OntoRefine<br>
   * 2. Validate that the service was added by retrieving the preferences<br>
   */
  @Test
  void registerNewReconciliationService() throws Exception {
    ReconServiceRegistrationCommandResponse registrationResponse =
        RefineCommands
            .registerReconciliationService()
            .setService(RECON_SERVICE)
            .setToken(getToken())
            .build()
            .execute(getClient());

    assertEquals(ResponseCode.OK, registrationResponse.getCode());

    GetPreferenceCommandResponse preferenceResponse =
        RefineCommands
            .getPreference()
            .setProperty("reconciliation.standardServices")
            .build()
            .execute(getClient());

    JsonNode valueNode = preferenceResponse.getResult().get("value");
    List<String> urls =
        JSON_PARSER.read(valueNode.asText("[]"), ArrayNode.class).findValuesAsText("url");

    // there are multiple keys 'url' so we're verifying that the one we've added put is there
    assertTrue(urls.contains(RECON_SERVICE));
  }

  /**
   * Scenario 2:<br>
   * <br>
   * 1. Creates project with name 'Scenario-2-{test-class-name}' using the
   * 'reduced_netherlands_resturants.csv'<br>
   * 2. Guess the type of the column 'City'<br>
   * 3. Reconcile the 'City' column against 'wikidata' reconciliation service<br>
   * 4. Wait until all operations are applied<br>
   * 5. Verify that reconciliation was applied<br>
   * 6. Delete the project
   */
  @Test
  void reconcileColumn() throws Exception {
    String projectName = "Scenario-2-" + ReconciliationIntegrationTests.class.getSimpleName();
    CreateProjectResponse createProject = createProject(projectName, RESTAURANTS_CSV);
    String projectId = createProject.getProjectId();
    assertNotNull(projectId);

    String column = "City";

    GuessColumnTypeCommandResponse columnTypeResponse =
        guessColumnType(projectId, column, "https://wikidata.reconci.link/en/api");
    assertTrue(
        StringUtils.isBlank(columnTypeResponse.getError()),
        "There are unexpected errors. Details: " + columnTypeResponse.getError());

    ReconciliationType type = columnTypeResponse.getTypes().get(0);
    ReconcileCommandResponse reconcileResponse = reconcile(projectId, type, column);
    assertNotEquals(ResponseCode.ERROR, reconcileResponse.getCode());

    waitForProcessesCompletion(projectId);

    GetOperationsResponse operationsResponse =
        RefineCommands.getOperations().setProject(projectId).build().execute(getClient());
    ArrayNode operations = (ArrayNode) operationsResponse.getContent();
    assertTrue(
        operations.findValuesAsText("op").contains("core/recon"),
        "The recon operation was not completed as expected.");

    deleteProject(projectId);
  }

  private GuessColumnTypeCommandResponse guessColumnType(
      String projectId, String column, String service) throws RefineException {
    return RefineCommands
        .guessTypeOfColumn()
        .setProject(projectId)
        .setColumn(column)
        .setService(service)
        .setToken(getToken())
        .build()
        .execute(getClient());
  }

  private ReconcileCommandResponse reconcile(
      String projectId, ReconciliationType type, String column) throws RefineException {
    return RefineCommands
        .reconcile()
        .setProject(projectId)
        .setColumn(column)
        .setColumnType(new ColumnType(type.getId(), type.getName()))
        .setToken(getToken())
        .build()
        .execute(getClient());
  }
}
