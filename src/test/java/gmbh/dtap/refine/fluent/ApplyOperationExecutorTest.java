package gmbh.dtap.refine.fluent;

import gmbh.dtap.refine.api.Operation;
import gmbh.dtap.refine.api.RefineClient;
import gmbh.dtap.refine.client.JsonOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.verify;

/**
 * Unit Tests for {@link ApplyOperationExecutor}.
 *
 * @since 0.1.7
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplyOperationExecutorTest {

   @Mock private RefineClient refineClientMock;
   private ApplyOperationExecutor applyOperationExecutor;

   @Before
   public void setUp() {
      applyOperationExecutor = new ApplyOperationExecutor();
   }

   @Test
   public void should_execute_when_ope_operation_is_set() throws IOException {
      String projectId = "1234567890";
      Operation operation = JsonOperation.from("{}");

      applyOperationExecutor
            .project(projectId)
            .operations(operation)
            .execute(refineClientMock);

      verify(refineClientMock).applyOperations(projectId, operation);
   }

   @Test
   public void should_execute_when_operations_are_set() throws IOException {
      String projectId = "1234567890";
      Operation operation1 = JsonOperation.from("{}");
      Operation operation2 = JsonOperation.from("{}");

      applyOperationExecutor
            .project(projectId)
            .operations(operation1, operation2)
            .execute(refineClientMock);

      verify(refineClientMock).applyOperations(projectId, operation1, operation2);
   }
}
