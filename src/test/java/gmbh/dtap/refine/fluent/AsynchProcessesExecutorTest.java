package gmbh.dtap.refine.fluent;

import gmbh.dtap.refine.api.RefineClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.verify;

/**
 * Unit Tests for {@link AsynchProcessesExecutor}.
 *
 * @since 0.1.8
 */
@RunWith(MockitoJUnitRunner.class)
public class AsynchProcessesExecutorTest {

   @Mock private RefineClient refineClientMock;
   private AsynchProcessesExecutor asynchProcessesExecutor;

   @Before
   public void setUp() {
      asynchProcessesExecutor = new AsynchProcessesExecutor();
   }

   @Test
   public void should_execute() throws IOException {
      String projectId = "1234567890";

      asynchProcessesExecutor
            .project(projectId)
            .execute(refineClientMock);

      verify(refineClientMock).asyncProcesses(projectId);
   }
}
