package gmbh.dtap.refine.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for {@link GetVersionCommand}.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetVersionCommandTest {

   @Mock
   private RefineClient refineClient;

   private GetVersionCommand command;

   @Before
   public void setUp() throws MalformedURLException {
      refineClient = mock(RefineClient.class);
      when(refineClient.createUrl(anyString())).thenReturn(new URL("http://localhost:3333/"));
      command = new GetVersionCommand();
   }

   @Test
   public void should() throws IOException {
      command.execute(refineClient);
      verify(refineClient).createUrl(anyString());
      verify(refineClient).execute(any(), any());
   }
}
