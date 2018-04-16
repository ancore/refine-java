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
 * Unit Tests for {@link ProjectMetadataExecutor}.
 *
 * @since 0.1.7
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectMetadataExecutorTest {

   @Mock private RefineClient refineClientMock;
   private ProjectMetadataExecutor projectMetadataExecutor;

   @Before
   public void setUp() {
      projectMetadataExecutor = new ProjectMetadataExecutor();
   }

   @Test
   public void should_execute() throws IOException {
      projectMetadataExecutor.execute(refineClientMock);
      verify(refineClientMock).getAllProjectMetadata();
   }
}
