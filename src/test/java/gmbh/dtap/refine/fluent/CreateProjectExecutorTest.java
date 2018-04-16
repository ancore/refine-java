package gmbh.dtap.refine.fluent;

import gmbh.dtap.refine.api.RefineClient;
import gmbh.dtap.refine.api.UploadFormat;
import gmbh.dtap.refine.api.UploadOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import static gmbh.dtap.refine.client.KeyValueUploadOptions.create;
import static org.mockito.Mockito.verify;

/**
 * Unit Tests for {@link CreateProjectExecutor}.
 *
 * @since 0.1.7
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateProjectExecutorTest {

   @Mock private RefineClient refineClientMock;
   private CreateProjectExecutor createProjectExecutor;

   @Before
   public void setUp() {
      createProjectExecutor = new CreateProjectExecutor();
   }

   @Test
   public void should_execute_when_minimum_is_set() throws IOException {
      String name = "Name";
      File file = new File("file.csv");

      createProjectExecutor
            .name("Name")
            .file(new File("file.csv"))
            .execute(refineClientMock);

      verify(refineClientMock).createProject(name, file, null, null);
   }

   @Test
   public void should_execute_when_some_is_set() throws IOException {
      String name = "Name";
      File file = new File("file.csv");
      UploadFormat format = UploadFormat.XML;

      createProjectExecutor
            .name("Name")
            .file(new File("file.csv"))
            .format(format)
            .execute(refineClientMock);

      verify(refineClientMock).createProject(name, file, format, null);
   }

   @Test
   public void should_execute_when_fully_set() throws IOException {
      String name = "Name";
      File file = new File("file.csv");
      UploadFormat format = UploadFormat.XML;
      UploadOptions options = create("separator", ",");

      createProjectExecutor
            .name("Name")
            .file(new File("file.csv"))
            .format(format)
            .options(options)
            .execute(refineClientMock);

      verify(refineClientMock).createProject(name, file, format, options);
   }
}
