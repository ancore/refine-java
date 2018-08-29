package gmbh.dtap.refine.client;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

/**
 * A command to delete a project.
 */
public class DeleteProjectCommand {

   private String projectId;

   /**
    * Sets the project ID.
    *
    * @param projectId the project ID
    * @return the command for fluent usage
    */
   public DeleteProjectCommand project(String projectId) {
      this.projectId = projectId;
      return this;
   }

   /**
    * Sets the project ID from the project location.
    *
    * @param projectLocation the project location
    * @return the command for fluent usage
    */
   public DeleteProjectCommand project(ProjectLocation projectLocation) {
      notNull(projectLocation, "projectLocation");
      this.projectId = projectLocation.getId();
      return this;
   }

   /**
    * Sets the project ID from the project.
    *
    * @param project the project
    * @return the command for fluent usage
    */
   public DeleteProjectCommand project(RefineProject project) {
      notNull(project, "project");
      this.projectId = project.getId();
      return this;
   }

   /**
    * Executes the command after validation.
    *
    * @param client the client to execute the command with
    * @return the result of the command
    * @throws IOException     in case of a connection problem
    * @throws RefineException in case the server responses with an error or is not understood
    */
   public DeleteProjectResponse execute(RefineClient client) throws IOException {
      notNull(projectId, "projectId");
      notEmpty(projectId, "projectId is empty");

      URL url = client.createUrl("/command/core/delete-project");

      List<NameValuePair> form = new ArrayList<>();
      form.add(new BasicNameValuePair("project", projectId));

      UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

      HttpUriRequest request = RequestBuilder
            .post(url.toString())
            .setHeader(ACCEPT, APPLICATION_JSON.getMimeType())
            .setEntity(entity)
            .build();

      return client.execute(request, new DeleteProjectResponseHandler());
   }
}
