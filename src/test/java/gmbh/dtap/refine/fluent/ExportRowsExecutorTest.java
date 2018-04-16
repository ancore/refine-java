package gmbh.dtap.refine.fluent;

import gmbh.dtap.refine.api.Engine;
import gmbh.dtap.refine.api.RefineClient;
import gmbh.dtap.refine.client.JsonEngine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static gmbh.dtap.refine.api.ExportFormat.CSV;
import static org.mockito.Mockito.verify;

/**
 * Unit Tests for {@link ExportRowsExecutor}.
 *
 * @since 0.1.7
 */
@RunWith(MockitoJUnitRunner.class)
public class ExportRowsExecutorTest {

   @Mock private RefineClient refineClientMock;
   private ExportRowsExecutor exportRowsExecutor;
   private ByteArrayOutputStream byteArrayOutputStream;

   @Before
   public void setUp() {
      exportRowsExecutor = new ExportRowsExecutor();
      byteArrayOutputStream = new ByteArrayOutputStream();
   }

   @Test
   public void should_execute_when_minimum_is_set() throws IOException {
      String projectId = "1234567890";

      exportRowsExecutor.project(projectId)
            .format(CSV)
            .outputStream(byteArrayOutputStream)
            .execute(refineClientMock);

      verify(refineClientMock).exportRows(projectId, null, CSV, byteArrayOutputStream);
   }

   @Test
   public void should_execute_when_fully_set() throws IOException {
      String projectId = "1234567890";
      Engine engine = JsonEngine.from("{}");

      exportRowsExecutor
            .project(projectId)
            .engine(engine)
            .format(CSV)
            .outputStream(byteArrayOutputStream)
            .execute(refineClientMock);

      verify(refineClientMock).exportRows(projectId, engine, CSV, byteArrayOutputStream);
   }
}
