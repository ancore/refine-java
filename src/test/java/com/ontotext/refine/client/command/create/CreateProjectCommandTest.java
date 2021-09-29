package com.ontotext.refine.client.command.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.UploadFormat;
import com.ontotext.refine.client.command.RefineCommands;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Unit Tests for {@link CreateProjectCommand}.
 */
class CreateProjectCommandTest {

  @Mock
  private RefineClient refineClient;

  private CreateProjectCommand command;

  @BeforeEach
  void setUp() throws URISyntaxException {
    MockitoAnnotations.openMocks(this);

    when(refineClient.createUri(anyString())).thenReturn(new URI("http://localhost:3333/"));

    command = RefineCommands.createProject().token("test-token").name("JSON Test (Main)")
        .file(new File("src/test/resources/addresses.csv")).format(UploadFormat.SEPARATOR_BASED)
        .options(
            () -> "{ \"encoding\": \"UTF-8\", \"projectTags\": \"[foo]\", \"separator\": \",\"}")
        .build();
  }

  @Test
  void should_execute() throws IOException {
    command.execute(refineClient);

    verify(refineClient).createUri(anyString());
    verify(refineClient).execute(any(), any());
  }
}
