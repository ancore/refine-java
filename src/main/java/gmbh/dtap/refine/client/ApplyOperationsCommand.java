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
import java.util.StringJoiner;

import static org.apache.commons.lang3.Validate.*;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

/**
 * A command to apply operations on a project.
 */
public class ApplyOperationsCommand {

   private String projectId;
   private Operation[] operations;

   /**
    * Sets the project ID.
    *
    * @param projectId the project ID
    * @return the command for fluent usage
    */
   public ApplyOperationsCommand project(String projectId) {
      this.projectId = projectId;
      return this;
   }

   /**
    * Sets the project ID from the project location.
    *
    * @param projectLocation the project location
    * @return the command for fluent usage
    */
   public ApplyOperationsCommand project(ProjectLocation projectLocation) {
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
   public ApplyOperationsCommand project(RefineProject project) {
      notNull(project, "project");
      this.projectId = project.getId();
      return this;
   }

   /**
    * Sets one or more operations.
    *
    * @param operations the operations
    * @return the command for fluent usage
    */
   public ApplyOperationsCommand operations(Operation... operations) {
      this.operations = operations;
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
   public ApplyOperationsResponse execute(RefineClient client) throws IOException {
      notNull(projectId, "projectId");
      notEmpty(projectId, "projectId is empty");
      notNull(operations, "operations");
      notEmpty(operations, "operations is empty");
      noNullElements(operations, "operations contains null");

      URL url = client.createUrl("/command/core/apply-operations");

      List<NameValuePair> form = new ArrayList<>();
      form.add(new BasicNameValuePair("project", projectId));
      StringJoiner operationsJoiner = new StringJoiner(",");
      for (Operation operation : operations) {
         operationsJoiner.add(operation.asJson());
      }
      String operationsJsonArray = "[" + operationsJoiner.toString() + "]";
      form.add(new BasicNameValuePair("operations", operationsJsonArray));

      UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

      HttpUriRequest request = RequestBuilder
            .post(url.toString())
            .setHeader(ACCEPT, APPLICATION_JSON.getMimeType())
            .setEntity(entity)
            .build();

      return client.execute(request, new ApplyOperationsResponseHandler());
   }
}
