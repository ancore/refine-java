package com.ontotext.refine.client.command.reconcile;

import static com.ontotext.refine.client.util.HttpParser.HTTP_PARSER;
import static com.ontotext.refine.client.util.JsonParser.JSON_PARSER;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ontotext.refine.client.RefineClient;
import com.ontotext.refine.client.ResponseCode;
import com.ontotext.refine.client.command.RefineCommands;
import com.ontotext.refine.client.command.preferences.GetPreferenceCommandResponse;
import com.ontotext.refine.client.command.preferences.SetPreferenceCommandResponse;
import com.ontotext.refine.client.exceptions.RefineException;
import java.io.IOException;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

/**
 * A command that registers new reconciliation service as standard. The service is set to the
 * preferences property named - 'reconciliation.standardServices'.
 *
 * @author Antoniy Kunchev
 */
public class ReconServiceRegistrationCommand {

  private static final String PROPERTY = "reconciliation.standardServices";

  private static final JsonNode UI_HANDLER_CONFIG;

  static {
    UI_HANDLER_CONFIG =
        JsonNodeFactory.instance.objectNode()
            .put("handler", "ReconStandardServicePanel")
            .put("access", "json");
  }

  private final String service;
  private final String token;

  private ReconServiceRegistrationCommand(String service, String token) {
    this.service = service;
    this.token = token;
  }

  /**
   * Executes the command.
   *
   * @param client to be used for command request
   * @return a command response
   * @throws RefineException when any error occurs during command execution
   */
  public ReconServiceRegistrationCommandResponse execute(RefineClient client)
      throws RefineException {
    try {
      GetPreferenceCommandResponse preference = getServicesPreference(client);

      ArrayNode values = getValues(preference);

      JsonNode serviceRegInfo = getServiceRegistrationInformation(client);
      appendAdditionalConfigurations(serviceRegInfo);
      values.add(serviceRegInfo);

      SetPreferenceCommandResponse response = registerService(client, values);

      return handleCompletion(response);
    } catch (RefineException re) {
      throw re;
    } catch (IOException | UnsupportedOperationException ioe) {
      throw new RefineException(
          "Failed to register reconciliation service: '%s' due to: %s",
          service,
          ioe.getMessage());
    }
  }

  private GetPreferenceCommandResponse getServicesPreference(RefineClient client)
      throws RefineException {
    return RefineCommands.getPreference().setProperty(PROPERTY).build().execute(client);
  }

  private ArrayNode getValues(GetPreferenceCommandResponse preference) throws IOException {
    JsonNode node = preference.getResult().get("value");

    // if there are no services registered, null is returned as string..
    if (node == null || "null".equalsIgnoreCase(node.asText())) {
      return JsonNodeFactory.instance.arrayNode();
    }
    return JSON_PARSER.read(node.asText("[]"), ArrayNode.class);
  }

  private JsonNode getServiceRegistrationInformation(RefineClient client) throws IOException {
    HttpUriRequest request = RequestBuilder.get(service).build();
    return client.execute(request, response -> {
      HTTP_PARSER.assureStatusCode(response, HttpStatus.SC_OK);
      return JSON_PARSER.parseJson(response.getEntity().getContent());
    });
  }

  /**
   * Appends the service URL as additional property to the retrieved service info. Also adds UI
   * handler configuration in order for the service to be properly rendered in the OntoRefine UI
   * (GDB Workbench).
   */
  private void appendAdditionalConfigurations(JsonNode serviceInfo) {
    if (serviceInfo != null && !serviceInfo.isEmpty() && serviceInfo.isObject()) {
      ((ObjectNode) serviceInfo).put("url", service).set("ui", UI_HANDLER_CONFIG);
    }
  }

  private SetPreferenceCommandResponse registerService(RefineClient client, Object value)
      throws RefineException {
    return RefineCommands
        .setPreference()
        .setProperty(PROPERTY)
        .setValue(value)
        .setToken(token)
        .build()
        .execute(client);
  }

  private ReconServiceRegistrationCommandResponse handleCompletion(
      SetPreferenceCommandResponse response) {
    return ResponseCode.OK.equals(response.getCode())
        ? ReconServiceRegistrationCommandResponse.ok()
        : ReconServiceRegistrationCommandResponse.error(response.getMessage());
  }

  /**
   * Builder for {@link ReconServiceRegistrationCommand}.
   *
   * @author Antoniy Kunchev
   */
  public static class Builder {

    private String service;
    private String token;

    public Builder setService(String service) {
      this.service = service;
      return this;
    }

    public Builder setToken(String token) {
      this.token = token;
      return this;
    }

    /**
     * Builds the {@link ReconServiceRegistrationCommand}.
     *
     * @return a command
     */
    public ReconServiceRegistrationCommand build() {
      Validate.notBlank(service, "Missing 'service' argument");
      Validate.notBlank(token, "Missing CSRF token");
      return new ReconServiceRegistrationCommand(service, token);
    }
  }
}
