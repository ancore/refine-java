package com.ontotext.refine.client.command.models;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ontotext.refine.client.command.BaseCommandTest;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link GetProjectModelsCommand}.
 *
 * @author Antoniy Kunchev
 */
class GetProjectModelsCommandTest
    extends BaseCommandTest<GetProjectModelsResponse, GetProjectModelsCommand> {

  @Override
  protected GetProjectModelsCommand command() {
    return RefineCommands.getProjectModels().setProject(PROJECT_ID).build();
  }

  @Override
  protected String getTestDir() {
    return "project-models/";
  }

  @Test
  void execute_successful() throws IOException {
    assertDoesNotThrow(() -> command().execute(client));

    verify(client).createUri(anyString());
    verify(client).execute(any(), any());
  }

  @Test
  void execute_failure() throws IOException {
    when(client.execute(any(), any())).thenThrow(new IOException("Test error"));

    assertThrows(RefineException.class, () -> command().execute(client));

    verify(client).createUri(anyString());
    verify(client).execute(any(), any());
  }

  @Test
  void handleResponse() throws IOException {
    InputStream body = loadResource("getProjectModels_response.json");

    GetProjectModelsResponse response = command().handleResponse(okResponse(body));

    assertNotNull(response);
    assertNotNull(response.getColumnModel());
    assertNotNull(response.getHttpHeaders());
    assertNotNull(response.getOverlayModels());
    assertNotNull(response.getRecordModel());
    assertNotNull(response.getScripting());
  }
}
