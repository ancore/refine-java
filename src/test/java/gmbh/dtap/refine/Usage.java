package gmbh.dtap.refine;

import gmbh.dtap.refine.api.RefineClient;
import gmbh.dtap.refine.api.RefineProject;
import gmbh.dtap.refine.api.UploadFormat;
import gmbh.dtap.refine.api.UploadOptions;

import java.io.File;

/**
 * Sample usage of the Refine API Java client.
 *
 * @since 0.1.0
 */
public class Usage {

   private String url = "http://localhost:3333/";
   private File file = new File("src/test/resources/addresses.csv");

   private void createAndDeleteProject() throws Exception {
      try (RefineClient client = RefineClients.create(url)) {
         RefineProject project = client.createProject("Addresses", file);
         client.deleteProject(project.getId());
      }
   }

   private void createAndDeleteExtendedProject() throws Exception {
      try (RefineClient client = RefineClients.create(url)) {
         RefineProject project = client.createProject("Addresses", file,
               UploadFormat.separatorBased, UploadOptions.create("separator", ","));
         client.deleteProject(project.getId());
      }
   }

   public static void main(String... args) throws Exception {
      Usage usage = new Usage();
      usage.createAndDeleteProject();
      usage.createAndDeleteExtendedProject();
   }

}
