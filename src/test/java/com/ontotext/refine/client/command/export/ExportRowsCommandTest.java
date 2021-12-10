package com.ontotext.refine.client.command.export;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test for {@link ExportRowsCommand}.
 *
 * @author Antoniy Kunchev
 */
class ExportRowsCommandTest {

  @Mock
  private RefineClient client;

  @Mock
  private HttpResponse response;

  @BeforeEach
  void setup() throws MalformedURLException {
    MockitoAnnotations.openMocks(this);

    when(client.createUrl(anyString())).thenReturn(new URL("http://localhost:3333/"));
    when(response.getStatusLine())
        .thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), HttpStatus.SC_OK, "OK"));
  }

  @Test
  void shouldRunSuccessfully() throws IOException {
    ExportRowsResponse rowsResponse = null;
    try {
      BasicHttpEntity entity = new BasicHttpEntity();
      entity.setContent(new ByteArrayInputStream("{\"City\":\"Amsterdam\"}".getBytes()));
      when(response.getEntity()).thenReturn(entity);

      when(client.execute(any(), any())).thenAnswer(
          answer -> answer.getArgument(1, ResponseHandler.class).handleResponse(response));

      rowsResponse = RefineCommands
          .exportRows()
          .setProject("1234567890")
          .setToken("csrf-token")
          .setFormat("json")
          .build()
          .execute(client);

      assertNotNull(rowsResponse);
      assertNotNull(rowsResponse.getFile());
    } finally {
      if (rowsResponse != null && rowsResponse.getFile() != null) {
        rowsResponse.getFile().deleteOnExit();
      }
    }
  }

  @Test
  void shouldFail() throws IOException {
    when(client.execute(any(), any())).thenThrow(new IOException());

    ExportRowsCommand command = RefineCommands
        .exportRows()
        .setProject("1234567890")
        .setToken("csrf-token")
        .setFormat("csv")
        .setEngine(Engines.ROW_BASED)
        .build();

    assertThrows(RefineException.class, () -> command.execute(client));
  }
}
