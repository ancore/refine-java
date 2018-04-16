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
 * Unit Tests for {@link DeleteProjectExecutor}.
 *
 * @since 0.1.7
 */
@RunWith(MockitoJUnitRunner.class)
public class DeleteProjectExecutorTest {

   @Mock private RefineClient refineClientMock;
   private DeleteProjectExecutor deleteProjectExecutor;

   @Before
   public void setUp() {
      deleteProjectExecutor = new DeleteProjectExecutor();
   }

   @Test
   public void should_execute() throws IOException {
      String projectId = "1234567890";

      deleteProjectExecutor
            .project(projectId)
            .execute(refineClientMock);

      verify(refineClientMock).deleteProject(projectId);
   }
}
