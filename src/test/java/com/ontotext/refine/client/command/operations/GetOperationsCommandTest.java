package com.ontotext.refine.client.command.operations;

import static com.ontotext.refine.client.util.JsonParser.JSON_PARSER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.ontotext.refine.client.command.BaseCommandTest;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link GetOperationsCommand}.
 *
 * @author Antoniy Kunchev
 */
class GetOperationsCommandTest
    extends BaseCommandTest<GetOperationsResponse, GetOperationsCommand> {

  @Override
  protected GetOperationsCommand command() {
    return RefineCommands.getOperations().setProject(PROJECT_ID).build();
  }

  @Override
  protected String getTestDir() {
    return "operations/";
  }

  @Test
  void execute_successful() throws IOException {
    assertDoesNotThrow(() -> command().execute(client));

    verify(client).createUrl(anyString());
    verify(client).execute(any(), any());
  }

  @Test
  void execute_failure() throws IOException {
    when(client.execute(any(), any())).thenThrow(new IOException("Test error"));

    RefineException exception =
        assertThrows(RefineException.class, () -> command().execute(client));

    assertEquals(
        "Failed to retrieve the operations for project: '" + PROJECT_ID + "' due to: Test error",
        exception.getMessage());

    verify(client).createUrl(anyString());
    verify(client).execute(any(), any());
  }

  @Test
  void handleResponse_successful() throws IOException {
    GetOperationsResponse response =
        command().handleResponse(okResponse(loadResource("getOperations_response.json")));

    JsonNode expected = JSON_PARSER.parseJson(loadResource("getOperations_expected.json"));
    assertEquals(expected, response.getContent());
  }
}
