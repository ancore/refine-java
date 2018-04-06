package gmbh.dtap.refine;

import gmbh.dtap.refine.api.*;

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

   private void createProjectAndExportRows() throws Exception {
      try (RefineClient client = RefineClients.create(url)) {
         RefineProject project = client.createProject("Addresses", file,
               UploadFormat.separatorBased, UploadOptions.create("separator", ","));
         client.exportRows(project.getId(), Engine.from("{\"facets\":[]}"), ExportFormat.html, System.out);
         client.deleteProject(project.getId());
      }
   }

   public static void main(String... args) throws Exception {
      Usage usage = new Usage();
      usage.createAndDeleteProject();
      usage.createAndDeleteExtendedProject();
      usage.createProjectAndExportRows();
   }

}
