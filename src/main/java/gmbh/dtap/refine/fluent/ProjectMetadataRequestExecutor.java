package gmbh.dtap.refine.fluent;

import gmbh.dtap.refine.api.RefineClient;
import gmbh.dtap.refine.api.RefineException;
import gmbh.dtap.refine.api.RefineProject;

import java.io.IOException;
import java.util.List;

/**
 * A request executor to get the metadata of all projects.
 * A fluent interface is not necessary as there are no parameters.
 *
 * @see RefineExecutor#projectMetadata()
 * @see RefineClient#getAllProjectMetadata()
 * @since 0.1.7
 */
public class ProjectMetadataRequestExecutor {

   /**
    * Executes the request.
    *
    * @param client the client to execute the request with
    * @return a list with all project metadata
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the request failed
    * @since 0.1.7
    */
   public List<RefineProject> execute(RefineClient client) throws IOException {
      return client.getAllProjectMetadata();
   }
}
