package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.RefineProject;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.util.Asserts.notNull;

/**
 * This class represents the response from the project metadata request.
 *
 * @since 0.1.2
 */
public class ProjectMetadataResponse {

   private final List<RefineProject> refineProjects;

   ProjectMetadataResponse() {
      refineProjects = new ArrayList<>();
   }

   public void add(RefineProject refineProject) {
      notNull(refineProject, "refineProject");
      refineProjects.add(refineProject);
   }

   public List<RefineProject> getRefineProjects() {
      return refineProjects;
   }

   @Override
   public String toString() {
      return "ProjectMetadataResponse{" +
            "refineProjects=" + refineProjects +
            '}';
   }
}
