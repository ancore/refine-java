package com.ontotext.refine.client.command.operations;

import static com.ontotext.refine.client.util.HttpParser.HTTP_PARSER;
import static com.ontotext.refine.client.util.JsonParser.JSON_PARSER;
import static org.apache.commons.lang3.StringUtils.appendIfMissing;
import static org.apache.commons.lang3.StringUtils.prependIfMissing;
import static org.apache.commons.lang3.Validate.noNullElements;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.JsonNode;
import com.ontotext.refine.client.Operation;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.command.RefineCommand;
import com.ontotext.refine.client.exceptions.RefineException;
import com.ontotext.refine.client.util.mappings.MappingsNormalizer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


/**
 * A command to apply operations on a project.
 */
public class ApplyOperationsCommand implements RefineCommand<ApplyOperationsResponse> {

  private final String projectId;
  private final Operation[] operations;
  private final String token;

  /**
   * Constructor for {@link Builder}.
   *
   * @param projectId the project ID
   * @param operations the operations
   * @param token the CSRF token to be used
   */
  private ApplyOperationsCommand(String projectId, Operation[] operations, String token) {
    this.projectId = projectId;
    this.operations = operations;
    this.token = token;
  }

  @Override
  public String endpoint() {
    return "/orefine/command/core/apply-operations";
  }

  @Override
  public ApplyOperationsResponse execute(RefineClient client) throws RefineException {
    try {
      List<NameValuePair> form = new ArrayList<>(2);
      form.add(new BasicNameValuePair(Constants.PROJECT, projectId));

      String ops =
          Arrays.stream(operations)
              .map(Operation::asJson)
              .map(MappingsNormalizer::forApplyOperations)
              .collect(Collectors.joining(","));

      String opsAsJsonArray = appendIfMissing(prependIfMissing(ops, "["), "]");
      form.add(new BasicNameValuePair("operations", opsAsJsonArray));

      UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

      HttpUriRequest request = RequestBuilder
          .post(client.createUri(endpoint()))
          .addParameter(Constants.CSRF_TOKEN, token)
          .setHeader(ACCEPT, APPLICATION_JSON.getMimeType())
          .setEntity(entity)
          .build();

      return client.execute(request, this);
    } catch (IOException ioe) {
      String error = String.format(
          "Failed to apply operations on project: '%s' due to: '%s'",
          projectId,
          ioe.getMessage());
      throw new RefineException(error);
    }
  }

  @Override
  public ApplyOperationsResponse handleResponse(HttpResponse response) throws IOException {
    HTTP_PARSER.assureStatusCode(response, SC_OK);
    String responseBody = EntityUtils.toString(response.getEntity());
    return parseApplyOperationsResponse(responseBody);
  }

  private ApplyOperationsResponse parseApplyOperationsResponse(String json) throws IOException {
    JsonNode node = JSON_PARSER.parseJson(json);
    String code = JSON_PARSER.findExistingPath(node, "code").asText();
    if ("ok".equals(code)) {
      return ApplyOperationsResponse.ok();
    }

    if ("pending".equals(code)) {
      return ApplyOperationsResponse.pending();
    }

    if ("error".equals(code)) {
      String message = JSON_PARSER.findExistingPath(node, "message").asText();
      return ApplyOperationsResponse.error(message);
    }

    throw new RefineException("Unexpected code: " + code);
  }

  /**
   * The builder for {@link ApplyOperationsCommand}.
   */
  public static class Builder {

    private String projectId;
    private Operation[] operations;
    private String token;

    /**
     * Sets the project ID.
     *
     * @param projectId the project ID
     * @return the builder for fluent usage
     */
    public Builder project(String projectId) {
      this.projectId = projectId;
      return this;
    }

    /**
     * Sets token.
     *
     * @param token the csrf token
     * @return the builder for fluent usage
     */
    public Builder token(String token) {
      this.token = token;
      return this;
    }

    /**
     * Sets one or more operations.
     *
     * @param operations the operations
     * @return the builder for fluent usage
     */
    public Builder operations(Operation... operations) {
      this.operations = operations;
      return this;
    }

    /**
     * Builds the command after validation.
     *
     * @return the command
     */
    public ApplyOperationsCommand build() {
      notBlank(projectId, "Missing 'projectId' argument");
      notNull(operations, "'operations' argument should not be null");
      notEmpty(operations, "'operations' argument should not be empty");
      noNullElements(operations, "'operations' should not contain 'null' elements");
      notBlank(token, "Missing CSRF token");
      return new ApplyOperationsCommand(projectId, operations, token);
    }
  }
}
