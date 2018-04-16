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
 * Unit Tests for {@link ExpressionPreviewExecutor}.
 *
 * @since 0.1.7
 */
@RunWith(MockitoJUnitRunner.class)
public class ExpressionPreviewExecutorTest {

   @Mock private RefineClient refineClientMock;
   private ExpressionPreviewExecutor expressionPreviewExeceExecutor;

   @Before
   public void setUp() {
      expressionPreviewExeceExecutor = new ExpressionPreviewExecutor();
   }

   @Test
   public void should_execute_when_minimum_is_set() throws IOException {
      String projectId = "1234567890";
      int cellIndex = 1;
      long[] rowIndices = {0, 1, 2};
      String grel = "toLowercase(value)";

      expressionPreviewExeceExecutor.project(projectId)
            .cellIndex(cellIndex)
            .rowIndices(rowIndices)
            .grel(grel)
            .execute(refineClientMock);

      verify(refineClientMock).expressionPreview(projectId, cellIndex, rowIndices, "grel:" + grel, false, 0);
   }

   @Test
   public void should_execute_when_repeat_is_set() throws IOException {
      String projectId = "1234567890";
      int cellIndex = 1;
      long[] rowIndices = {0, 1, 2};
      String expression = "grel:toLowercase(value)";

      expressionPreviewExeceExecutor.project(projectId)
            .cellIndex(cellIndex)
            .rowIndices(rowIndices)
            .expression(expression)
            .repeat(true)
            .repeatCount(10)
            .execute(refineClientMock);

      verify(refineClientMock).expressionPreview(projectId, cellIndex, rowIndices, expression, true, 10);
   }
}
